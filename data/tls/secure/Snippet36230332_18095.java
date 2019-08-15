 trait MySslConfiguration {

  def serverContext: HttpsContext = {
    val password = "password".toCharArray
    val context = SSLContext.getInstance("TLSv1.2")
    val keyStore = KeyStore.getInstance("jks")
    val keyStoreResource = "/scruples_keystore.jks"
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    keyStore.load(getClass.getClassLoader.getResourceAsStream(keyStoreResource), password)
    keyManagerFactory.init(keyStore, password)
    trustManagerFactory.init(keyStore)
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getManagers, new SecureRandom())
    val sslParams = context.getDefaultSSLParameters
    sslParams.setEndpointIdentificationAlgorithm("HTTPS")
    HttpsContext(sslContext = context
    sslParameters = Some(sslParams),
     enabledProtocols = Some(List("TLSv1.2", "TLSv1.1", "TLSv1")))
  }

}
