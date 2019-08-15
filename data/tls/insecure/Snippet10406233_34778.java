TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance("JKS");
FileInputStream fis = new FileInputStream("/.../truststore.jks");
ks.load(fis, null);
// or ks.load(fis, "thepassword".toCharArray());
fis.close();

tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
