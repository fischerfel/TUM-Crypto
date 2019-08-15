    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(null, null, null);
    SSLEngine engine = sslContext.createSSLEngine();
