TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance("JKS");
InputStream is = ...
ks.load(is, null);
// or ks.load(is, "thepassword".toCharArray());
is.close();

tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);

SSLSocketFactory = sslContext.getSocketFactory();
// ...
