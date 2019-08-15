KeyStore keyStore = KeyStore.getInstance("JKS");
InputStream stream = null;
stream = new FileInputStream("C:\\certs\\client.keystore");
keyStore.load(stream, "changeit".toCharArray());                
System.out.println("loaded the keystore");

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
PrivateKey key = (PrivateKey) keyStore.getKey("client-identity", "changeit".toCharArray());
System.out.println("private key:  " + key);
Certificate cert = (Certificate) keyStore.getCertificate("client-identity");
System.out.println("client cert:  " + cert);
kmf.init(keyStore, "changeit".toCharArray());

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(kmf.getKeyManagers(), null, null);          

SSLSocketFactory sslSF = (SSLSocketFactory) sslCtx.getSocketFactory();
System.out.println(" Creating and opening new SSLSocket with SSLSocketFactory");

// using createSocket(String hostname, int port)
SSLSocket sslSock = (SSLSocket) sslSF.createSocket("localhost", 1111);
System.out.println(" SSLSocket created");
HandshakeCompletedListener mListener = null;
mListener = new MyListener();

sslSock.addHandshakeCompletedListener(new MyListener());
