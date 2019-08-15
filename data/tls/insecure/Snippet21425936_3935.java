String path = ....
char[] password = ....
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(new FileInputStream(path), password );
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(null, tmf.getTrustManagers(), null);
