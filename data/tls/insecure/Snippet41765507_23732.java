val keyStore = KeyStore.getInstance("jks")
keyStore.load(inputStream, "storePass")

val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
keyManagerFactory.init(keyStore, keyPass?)

val keyManagers = keyManagerFactory.getKeyManagers

val sslContext = SSLContext.getInstance("TLS")
sslContext.init(keyManagers, null, new SecureRandom)
