KeyStore ks = KeyStore.getInstance("PKCS12");
InputStream is = ...;
char[] ksp = storePassword.toCharArray();
ks.load(is, ksp);
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
char[] kp = keyPassword.toCharArray();
kmf.init(ks, kp);
sslContext = SSLContext.getInstance("SSLv3");
sslContext.init(kmf.getKeyManagers(), null, null);
SSLSocketFactory factory = sslContext.getSocketFactory();
SSLSocket sslsocket = (SSLSocket) factory.createSocket(socket, socket
    .getInetAddress().getHostName(), socket.getPort(), true);
sslsocket.setUseClientMode(true);
sslsocket.setSoTimeout(soTimeout);
sslsocket.startHandshake();
