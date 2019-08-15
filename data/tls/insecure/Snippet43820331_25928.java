System.setProperty("jsse.enableSNIExtension", "false");
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},     new SecureRandom());
SSLContext.setDefault(ctx);

SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
sslSocket = (SSLSocket) factory.createSocket(socket,socketHost,port,true);
sslSocket.setSoTimeout(timeout);
sslSocket.startHandshake();
