

    class SslAuthenticatingHttp(certData: SslCertificateData) extends Http {
      override val client = new AsyncHttpClient(
        (new AsyncHttpClientConfig.Builder).setSSLContext(buildSslContext(certData)).build
      )

      private def buildSslContext(certData: SslCertificateData): SSLContext = {
        import certData._

        val clientCertStore = loadKeyStore(clientCertificateData, clientCertificatePassword)
        val rootCertStore = loadKeyStore(rootCertificateData, rootCertificatePassword)

        val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
        keyManagerFactory.init(clientCertStore, clientCertificatePassword.toCharArray)
        val keyManagers = keyManagerFactory.getKeyManagers()

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(rootCertStore)
        val trustManagers = trustManagerFactory.getTrustManagers()

        val context = SSLContext.getInstance("TLS")
        context.init(keyManagers, trustManagers, null)

        context
      }

      private def loadKeyStore(keyStoreData: Array[Byte], password: String): KeyStore = {
        val store = KeyStore.getInstance(KeyStore.getDefaultType)
        store.load(new ByteArrayInputStream(keyStoreData), password.toCharArray)
        store
      }
    }

    case class SslCertificateData (
      clientCertificateData: Array[Byte],
      clientCertificatePassword: String,
      rootCertificateData: Array[Byte],
      rootCertificatePassword: String)

