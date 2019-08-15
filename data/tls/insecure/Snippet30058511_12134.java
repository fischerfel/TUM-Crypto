trait SSLConfiguration {

  implicit def sslContext: SSLContext = {
    val keystore = keystore.jks
    val password = pas

    val keyStore = KeyStore.getInstance("jks")
    val in = getClass.getClassLoader.getResourceAsStream(keystore)
    require(in != null, "Bad java key storage file: " + keystore)
    keyStore.load(in, password.toCharArray)

    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(keyStore, password.toCharArray)
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(keyStore)
    val context = SSLContext.getInstance("TLS")
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }

  implicit def sslEngineProvider: ServerSSLEngineProvider = {
    ServerSSLEngineProvider { engine =>
      engine.setEnabledCipherSuites(Array("TLS_RSA_WITH_AES_256_CBC_SHA"))
  engine.setEnabledProtocols(Array("SSLv3", "TLSv1"))
      engine
    }
  }
}
