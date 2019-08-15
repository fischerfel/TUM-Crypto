  val sslContext = {
    val keyStore = KeyStore.getInstance("pkcs12")
    keyStore.load(new FileInputStream(clientKey), clientKeyPass.to[Array])
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmf.init(keyStore, clientKeyPass.to[Array])

    val trustStore = KeyStore.getInstance("jks")
    trustStore.load(new FileInputStream(trustStoreFile), trustStorePass.to[Array])
    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmf.init(trustStore)

    val ctx = SSLContext.getInstance("TLSv1.2")
    ctx.init(kmf.getKeyManagers, tmf.getTrustManagers, new SecureRandom())
    ctx
  }
