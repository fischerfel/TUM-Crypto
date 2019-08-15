keyStore = KeyStore.getInstance("BKS");
keyStore.load(stream, keyStorePwd.toCharArray());
keyManagerFactory = KeyManagerFactory
           .getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, keyStorePwd.toCharArray());
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(keyManagerFactory.getKeyManagers(), null, null);
server.makeSecure(sc.getServerSocketFactory());
server.start();
