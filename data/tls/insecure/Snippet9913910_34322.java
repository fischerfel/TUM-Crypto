HttpClient client = new HttpClient();
// truststore
KeyStore trustStore = KeyStore.getInstance("JKS", "SUN");
trustStore.load(TestSupertype.class.getResourceAsStream("/client-truststore.jks"), "amber%".toCharArray());
String alg = KeyManagerFactory.getDefaultAlgorithm();
TrustManagerFactory fac = TrustManagerFactory.getInstance(alg);
fac.init(trustStore);
// keystore
KeyStore keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
keystore.load(X509Test.class.getResourceAsStream("/etomcat_client.p12"), "etomcat".toCharArray());
String keyAlg = KeyManagerFactory.getDefaultAlgorithm();
KeyManagerFactory keyFac = KeyManagerFactory.getInstance(keyAlg);
keyFac.init(keystore, "etomcat".toCharArray());
// context
SSLContext ctx = SSLContext.getInstance("TLS", "SunJSSE");
ctx.init(keyFac.getKeyManagers(), fac.getTrustManagers(), new SecureRandom());
SslContextedSecureProtocolSocketFactory secureProtocolSocketFactory = new SslContextedSecureProtocolSocketFactory(ctx);
Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) secureProtocolSocketFactory, 8443));
// test get
HttpMethod get = new GetMethod("https://127.0.0.1:8443/etomcat_x509");
client.executeMethod(get);
// get response body and do what you need with it
byte[] responseBody = get.getResponseBody();
