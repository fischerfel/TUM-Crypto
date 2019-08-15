@Override public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    String password = "blablabla";

    KeyStore ks = KeyStore.getInstance("JKS");
    // Use nettyserver.jks do client side authentication
    InputStream readStream = getClass().getResourceAsStream("serverCert.jks");
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
    sslEngine.setUseClientMode(false);
    sslEngine.setNeedClientAuth(true);
    sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
    sslEngine.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA"});
    sslEngine.setEnableSessionCreation(true);

    // Add SSL handler into pipeline
    pipeline.addFirst("SSL", new SslHandler(sslEngine));

    // Add protobuf handler into pipeline
    pipeline.addLast(new ProtobufVarint32FrameDecoder());
    pipeline.addLast(new ProtobufDecoder(Messaging.BaseMessage.getDefaultInstance()));
    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
    pipeline.addLast(new ProtobufEncoder());

    // Add custom handler
    pipeline.addLast(new ServerHandler());
}
