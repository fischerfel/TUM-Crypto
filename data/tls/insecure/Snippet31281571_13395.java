trait SslConfiguration {
  implicit def sslContext: SSLContext = {
    val password = "changeit"
    val keyStoreResource = "/example.com.jks"

    val keyStore = KeyStore.getInstance("jks")
    keyStore.load(new FileInputStream(keyStoreResource), password.toCharArray)
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    keyManagerFactory.init(keyStore, password.toCharArray)
    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    trustManagerFactory.init(keyStore)
    val context = SSLContext.getInstance("TLS")

    context.init(keyManagerFactory.getKeyManagers, null, new SecureRandom)
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
