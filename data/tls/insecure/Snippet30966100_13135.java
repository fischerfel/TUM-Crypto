...

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(), new SecureRandom());
    SSLEngine sslEngine = sslContext.createSSLEngine();
    sslEngine.setUseClientMode(true);
    SslHandler sslHandler = new SslHandler(sslEngine);
    channel.pipeline().addFirst(sslHandler)

...
