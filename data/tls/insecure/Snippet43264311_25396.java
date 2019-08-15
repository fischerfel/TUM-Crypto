  def tlsStage(role: TLSRole)(implicit system: ActorSystem) = {
    val sslConfig = AkkaSSLConfig.get(system)
    val config = sslConfig.config

    // create a ssl-context that ignores self-signed certificates
    implicit val sslContext: SSLContext = {
        object WideOpenX509TrustManager extends X509TrustManager {
            override def checkClientTrusted(chain: Array[X509Certificate], authType: String) = ()
            override def checkServerTrusted(chain: Array[X509Certificate], authType: String) = ()
            override def getAcceptedIssuers = Array[X509Certificate]()
        }

        val context = SSLContext.getInstance("TLS")
        context.init(Array[KeyManager](), Array(WideOpenX509TrustManager), null)
        context
    }
    // protocols
    val defaultParams = sslContext.getDefaultSSLParameters()
    val defaultProtocols = defaultParams.getProtocols()
    val protocols = sslConfig.configureProtocols(defaultProtocols, config)
    defaultParams.setProtocols(protocols)

    // ciphers
    val defaultCiphers = defaultParams.getCipherSuites()
    val cipherSuites = sslConfig.configureCipherSuites(defaultCiphers, config)
    defaultParams.setCipherSuites(cipherSuites)

    val firstSession = new TLSProtocol.NegotiateNewSession(None, None, None, None)
       .withCipherSuites(cipherSuites: _*)
       .withProtocols(protocols: _*)
       .withParameters(defaultParams)

    val clientAuth = getClientAuth(config.sslParametersConfig.clientAuth)
    clientAuth map { firstSession.withClientAuth(_) }

    val tls = TLS.apply(sslContext, firstSession, role)

    val pf: PartialFunction[TLSProtocol.SslTlsInbound, ByteString] = {
      case TLSProtocol.SessionBytes(_, sb) => ByteString.fromByteBuffer(sb.asByteBuffer)
    }

    val tlsSupport = BidiFlow.fromFlows(
        Flow[ByteString].map(TLSProtocol.SendBytes),
        Flow[TLSProtocol.SslTlsInbound].collect(pf));

    tlsSupport.atop(tls);
  }

  def getClientAuth(auth: ClientAuth) = {
     if (auth.equals(ClientAuth.want)) {
         Some(TLSClientAuth.want)
     } else if (auth.equals(ClientAuth.need)) {
         Some(TLSClientAuth.need)
     } else if (auth.equals(ClientAuth.none)) {
         Some(TLSClientAuth.none)
     } else {
         None
     }
  }
