SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[] { trustManager }, null);
SSLContext.setDefault(sslContext);
