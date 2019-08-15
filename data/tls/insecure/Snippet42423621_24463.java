 def getSSLContext() : HttpsConnectionContext = {

    val ks: KeyStore = KeyStore.getInstance("JKS")

    val keystorePath = config.getString("keystore.path")
    val keystorePassword = config.getString("keystore.password").toCharArray

    val keystore: InputStream = Files.newInputStream( Paths.get(keystorePath))

    require(keystore != null, "Keystore required!")
    ks.load(keystore, keystorePassword)

    val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, keystorePassword)

    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ks)

    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
    ConnectionContext.https(sslContext)
  }
