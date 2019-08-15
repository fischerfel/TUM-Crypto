KeyStore identity = KeyStore.getInstance(KeyStore.getDefaultType());
/* Load the keystore (a different one for each site). */
...
SSLContext ctx = SSLContext.getInstance("TLS");
KeyManagerFactory kmf = 
  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(identity, password);
ctx.init(kmf.getKeyManagers(), null, null);
SSLServerSocketFactory factory = ctx.getServerSocketFactory();
ServerSocket server = factory.createSocket(port);
