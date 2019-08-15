KeyManagerFactory kmf = 
  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(identityStore, password);
TrustManagerFactory tmf =
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
