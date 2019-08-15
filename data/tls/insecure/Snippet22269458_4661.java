sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(new KeyManager[] {this}, new TrustManager[] {this}, new SecureRandom());
