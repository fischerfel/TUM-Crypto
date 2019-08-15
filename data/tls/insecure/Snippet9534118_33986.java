public void init(String backendHost, int backendPort, int timeOutSecs, boolean useTLS,
                boolean acceptOnlyTrustworthyCertsForTLS) throws ConnectionFailedException {

    channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool());

    if (useTLS) {
        initTLS(acceptOnlyTrustworthyCertsForTLS);
    }

    ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

        @Override
        public ChannelPipeline getPipeline() {
            ChannelPipeline pipeline = Channels.pipeline();

            if (sslContext != null) {
                SSLEngine sslEngine = sslContext.createSSLEngine();
                sslEngine.setUseClientMode(true);
                sslEngine.setEnabledCipherSuites(new String[] {
                    "TLS_RSA_WITH_AES_128_CBC_SHA"
                });
                pipeline.addLast("ssl", new SslHandler(sslEngine));
            }

            pipeline.addLast("compressor", new ZlibEncoder());
            pipeline.addLast("decompressor", new ZlibDecoder());

            pipeline.addLast("decoder", new JBossSerializationDecoder());
            pipeline.addLast("encoder", new JBossSerializationEncoder());

            pipeline.addLast("handler", StatefulTcpBackendCommunicator.this);
            return pipeline;
        }
    });
    bootstrap.setOption("tcpNoDelay", true);
    bootstrap.setOption("keepAlive", true);

    channelFuture = bootstrap.connect(new InetSocketAddress(backendHost, backendPort));
    try {
        boolean connected = channelFuture.await(timeOutSecs * 1000);
        if (!connected || !channelFuture.isSuccess()) {
            throw new ConnectionFailedException();
        }
    }
    catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
    }
}


private void initTLS(boolean acceptOnlyTrustworthyCertsForTLS) {
    try {
        TrustManager[] trustManagers;
        if (acceptOnlyTrustworthyCertsForTLS) {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                            .getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            trustManagers = trustManagerFactory.getTrustManagers();
        }
        else {
            trustManagers = new TrustManager[] {
                new X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }


                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        return;
                    }


                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        return;
                    }
                }
            };
        }

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());
    }
    catch (GeneralSecurityException e) {
        logger.error("TLS connection could not be established. TLS is not used!", e);
        sslContext = null;
    }
}
