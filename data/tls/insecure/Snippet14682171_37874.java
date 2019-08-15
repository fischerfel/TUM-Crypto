KeyManagerFactory kmf =  
    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(this.getCertificateContent(), "test".toCharArray());
kmf.init(keyStore, "test".toCharArray());

TrustManagerFactory tmf =
    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLSv1");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
SSLSocketFactory factory = ctx.getSocketFactory();
socket = (SSLSocket)factory.createSocket("100.125.100.1", 8775);
