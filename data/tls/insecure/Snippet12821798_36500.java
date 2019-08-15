public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();
    if (isSecure) {
        SSLContext clientContext = SSLContext.getInstance("TLS");
        clientContext.init(null, new TrustManager[]{DUMMY_TRUST_MANAGER}, null);
        SSLEngine engine = clientContext.createSSLEngine();
        engine.setUseClientMode(true);
        pipeline.addLast("ssl", new SslHandler(engine));
    }

    pipeline.addLast("codec", new HttpClientCodec());
    pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
    pipeline.addLast("timeout", new ReadTimeoutHandler(timer, timeout, TimeUnit.MILLISECONDS));
    pipeline.addLast("handler", ibConnectorHandler);
    return pipeline;
}
