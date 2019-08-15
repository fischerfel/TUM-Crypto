public static SSLContext initSSLContext(String keystoreLocation, String keystorePwd, String truststorePwd, String serverCrtPwd)
SSLContext context;
context = SSLContext.getInstance("TLS");
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream(keystoreLocation), keystorePwd.toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, serverCrtPwd.toCharArray());

KeyStore trustStore = KeyStore.getInstance("jks");
trustStore.load(new FileInputStream(keystoreLocation), truststorePwd.toCharArray());
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

context.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
