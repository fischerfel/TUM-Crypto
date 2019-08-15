final SSLContext ctx_ = SSLContext.getInstance("TLS");
ctx_.init(null, new TrustManager[] { new LocalSSLTrustManager() }, null);
final SSLSocketFactory factory = ctx_.getSocketFactory();
final SSLSocket sslSocket = (SSLSocket) factory.createSocket(hostAddress, port);
sslSocket.setUseClientMode(true);
sslSocket.setEnabledProtocols(new String[] { "TLSv1" });
sslSocket.startHandshake();
