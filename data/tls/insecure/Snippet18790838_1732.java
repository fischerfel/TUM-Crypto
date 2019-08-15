KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("keystoreFile"), "keystorePassword".toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(ks, "keystorePassword".toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509"); 
tmf.init(ks);

SSLContext sc = SSLContext.getInstance("TLS"); 
TrustManager[] trustManagers = tmf.getTrustManagers(); 
sc.init(kmf.getKeyManagers(), trustManagers, null); 

SSLServerSocketFactory ssf = sc.getServerSocketFactory(); 
SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(serverport);
SSLSocket c = (SSLSocket) s.accept();
