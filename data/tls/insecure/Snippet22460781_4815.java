KeyStore ks = LoadKeyStore(new File(serverKeyStore), pwdKeyStore, "JCEKS");
KeyManagerFactory kmf; 
kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, pwdKeyStore.toCharArray());

SSLContext sc = SSLContext.getInstance("SSL");
sc.init(kmf.getKeyManagers(),null, null);   

SSLServerSocketFactory ssf = sc.getServerSocketFactory(); 
sslserversocket = (SSLServerSocket) ssf.createServerSocket(port);
