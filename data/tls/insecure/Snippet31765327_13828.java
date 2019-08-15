KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
String algorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
tmf.init(keyStore);

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);
