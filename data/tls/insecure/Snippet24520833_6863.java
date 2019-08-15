SSLContext sslctx = SSLContext.getInstance("TLS");
sslctx.init(new KeyManager[0], new TrustManager[] {new AcceptAllTrustManager()}, new SecureRandom());
SSLSocketFactory factory = sslctx.getSocketFactory();
SSLSocket client = (SSLSocket) factory.createSocket("IP_ADDRESS", 8081);
