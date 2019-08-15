KeyStore ks = KeyStore.getInstance("JKS");
ks.load(testService.class.getClassLoader().getResourceAsStream("resources/.keystore"), "changeit".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "changeit".toCharArray());
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
SSLContext.setDefault(ctx); 
