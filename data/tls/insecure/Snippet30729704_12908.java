  private val keyStore = {
    //Setting up BouncyCastle provider for message signing
    Security.addProvider(new BouncyCastleProvider())
    //Loading keystore from specified file
    val clientStore = KeyStore.getInstance("JKS")
    val inputStream = new FileInputStream(MyClient.keystore)
    clientStore.load(inputStream, MyClient.keystorePassword.toCharArray)
    inputStream.close()
    clientStore
  }

  //Retrieving certificate and key
  private val cert = keyStore.getCertificate(MyClient.keyAlias).asInstanceOf[X509Certificate]
  private val key = keyStore.getKey(MyClient.keyAlias, MyClient.keystorePassword.toCharArray).asInstanceOf[PrivateKey]

  //Creating SSL context
  private val sslContext = {
    val context = SSLContext.getInstance("TLS")
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    val kmf: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmf.init(keyStore, MyClient.keystorePassword.toCharArray)
    tmf.init(keyStore)
    context.init(kmf.getKeyManagers, tmf.getTrustManagers, null)
    context
  }
