/* Load the keyStore that includes the server cert as a "trusted" entry. */
KeyStore keyStore = ... 
TrustManagerFactory tmf = 
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, tmf.getTrustManagers(), null);
sslFactory = ctx.getSocketFactory();
