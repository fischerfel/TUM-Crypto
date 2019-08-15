private void addSslHandlerOneWay(SocketChannel ch) throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(new File("svrks.jks")), "kspassword1".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "kspassword2".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    SSLEngine sslEngine = sslContext.createSSLEngine();
    sslEngine.setUseClientMode(false);
    sslEngine.setNeedClientAuth(false);//one-way
    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
    sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
    sslEngine.setEnableSessionCreation(true);

    ch.pipeline().addFirst("SSL", new SslHandler(sslEngine));
}
