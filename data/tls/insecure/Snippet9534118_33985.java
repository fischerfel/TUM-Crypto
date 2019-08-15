public StatefulTcpServer(MessageHandler messageHandler, int port, KeyStore keyStore, String keyStorePassword) {
    this.messageHandler = messageHandler;
    factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    allChannels = new DefaultChannelGroup("clients");

    initTLS(keyStore, keyStorePassword);

    ServerBootstrap bootstrap = new ServerBootstrap(factory);

    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

        @Override
        public ChannelPipeline getPipeline() {
            ChannelPipeline pipeline = Channels.pipeline();
            if (sslContext != null) {
                SSLEngine sslEngine = sslContext.createSSLEngine();
                sslEngine.setUseClientMode(false);
                sslEngine.setEnabledCipherSuites(new String[] {
                    "TLS_RSA_WITH_AES_128_CBC_SHA"
                });
                pipeline.addLast("ssl", new SslHandler(sslEngine));
            }

            pipeline.addLast("compressor", new ZlibEncoder());
            pipeline.addLast("decompressor", new ZlibDecoder());

            pipeline.addLast("decoder", new JBossSerializationDecoder());
            pipeline.addLast("encoder", new JBossSerializationEncoder());

            pipeline.addLast("handler", StatefulTcpServer.this);
            return pipeline;
        }
    });

    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);

    bootstrap.bind(new InetSocketAddress(port));
}

private void initTLS(KeyStore keyStore, String keyStorePassword) {
    try {
        if (keyStore != null) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword.toCharArray());
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        }
    }
    catch (GeneralSecurityException e) {
        logger.error("TLS connection could not be established", e);
        sslContext = null;
    }
}
