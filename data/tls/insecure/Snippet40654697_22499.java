@Override public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    String password = "blablabla";

    KeyStore ks = KeyStore.getInstance("JKS");
    InputStream readStream = getClass().getResourceAsStream("clientCert.jks");
    ks.load(readStream,
            password.toCharArray());

    TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory
            .getDefaultAlgorithm());
    tmFactory.init(ks);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, password.toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmFactory.getTrustManagers(), null);

    SSLEngine sslEngine = sslContext.createSSLEngine();
    sslEngine.setUseClientMode(true);

    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
    sslEngine.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA"});
    sslEngine.setEnableSessionCreation(true);

    pipeline.addFirst("SSL", new SslHandler(sslEngine));

    pipeline.addLast(new ProtobufVarint32FrameDecoder());
    pipeline.addLast(new ProtobufDecoder(Messaging.BaseMessage.getDefaultInstance()));
    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
    pipeline.addLast(new ProtobufEncoder());

    // and then business logic.
    pipeline.addLast(new ServerHandlerCorrelator());
}
