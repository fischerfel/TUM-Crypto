val clientStore = KeyStore.getInstance("PKCS12")

clientStore.load(new FileInputStream("/home/zlaja/Downloads/imakstore_80009164.p12"), "12348765".toCharArray())

val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
kmf.init(clientStore, "12348765".toCharArray())
val kms = kmf.getKeyManagers()

val trustStore = KeyStore.getInstance("JKS")

trustStore.load(new FileInputStream("/usr/user/programs/java/jdk1.7.0_10/jre/lib/security/cacerts"), "changeit".toCharArray())

val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
tmf.init(trustStore)
val tms = tmf.getTrustManagers()

val sslContext = SSLContext.getInstance("TLS")
sslContext.init(kms, tms, new SecureRandom())

val schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("https", new SSLSocketFactory(init), 443))

val client = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParameters,  schemeRegistry), httpParameters);
