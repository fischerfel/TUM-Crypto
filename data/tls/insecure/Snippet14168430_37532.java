KeyStore ks = KeyStore.getInstance("JKS");
ks.load(newFileInputStream("server"),"password".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "password".toCharArray());
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(kmf.getKeyManagers(), null, null);
SSLServerSocketFactory ssf = sc.getServerSocketFactory();
server = (SSLServerSocket) ssf.createServerSocket(this.port);
