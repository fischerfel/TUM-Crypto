KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
tks.load(...); /* Load the trust key store with root CAs. */
TrustManagerFactory tmf = 
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(tks);
KeyStore iks = KeyStore.getInstance(KeyStore.getDefaultType());
iks.load(...); /* Load the identity key store with your key/cert. */
KeyManagerFactory kmf = 
  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(iks, password);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
SocketFactory factory = ctx.getSocketFactory();
Socket socket = factory.createSocket(host, port);
