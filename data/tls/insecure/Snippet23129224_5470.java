KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(new FileInputStream(certificatePath), password.toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
kmf.init(ks, password.toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509"); 
tmf.init((KeyStore)null);

SSLContext sc = SSLContext.getInstance("TLS"); 
sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null); 

SSLSocketFactory ssf = sc.getSocketFactory(); 
SSLSocket socket = (SSLSocket) ssf.createSocket(InetAddress.getLocalHost(), 2195);
socket.startHandshake();
