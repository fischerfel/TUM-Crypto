SSLContext ctx = SSLContext.getInstance("TLSv1.2");
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance("JKS");

ks.load(new FileInputStream(serverKeystoreFile), serverKeystorePass);
kmf.init(ks, serverCertificatePass);
ks.load(new FileInputStream(serverTruststoreFile), serverTruststorePass);
tmf.init(ks);
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
SSLServerSocket sslserversocket = (SSLServerSocket) ssf.createServerSocket(port);
sslserversocket.setNeedClientAuth(true);

// accept connection from client
SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

// At this point, I would like to determine the connected client's certificate alias
// or some other property that is unique for each of the acceptable client certificates.
