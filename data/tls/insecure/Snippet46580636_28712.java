private void addSslHandlerOneWay(SocketChannel ch) throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(getInputStream("clits.jks"), "tspassword2".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ts, "tspassword1".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ts);

    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    SSLEngine sslEngine = sslContext.createSSLEngine();
    sslEngine.setUseClientMode(true);//client

    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
    sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
    sslEngine.setEnableSessionCreation(true);

    ch.pipeline().addFirst("SSL", new SslHandler(sslEngine));
}
