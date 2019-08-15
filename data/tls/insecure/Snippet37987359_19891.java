//Load JKS keystore that includes the server certificate or the root
KeyStore keyStore = ... 
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, tmf.getTrustManagers(), null);
sslFactory = ctx.getSocketFactory();
