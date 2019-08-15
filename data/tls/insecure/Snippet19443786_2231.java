

    public class CtraderClient {
        private final String host;
        private final int port;
        Channel channel = null;

        public CtraderClient(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void run() {
            // Configure the client.
            ClientBootstrap bootstrap = new ClientBootstrap(
                    new NioClientSocketChannelFactory(
                            Executors.newCachedThreadPool(),
                            Executors.newCachedThreadPool()));

            bootstrap.setOption("keepAlive", true);

            HashedWheelTimer timer = new HashedWheelTimer();
            // Configure the pipeline factory.
            bootstrap.setPipelineFactory(new SecurePipelineFactory(timer));

            // Start the connection attempt.
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                    port));

            // Wait until the connection attempt succeeds or fails.
            channel = future.awaitUninterruptibly().getChannel();
            if (!future.isSuccess()) {
                future.getCause().printStackTrace();
                bootstrap.releaseExternalResources();
                return;
            }

        }
    }

    public class SecurePipelineFactory implements ChannelPipelineFactory {

        private final ChannelHandler idleStateHandler;
        private Timer timer;

        public SecurePipelineFactory(Timer timer) {
            this.timer = timer;
            this.idleStateHandler = new IdleStateHandler(timer, 0, 20, 0); // timer must be shared.
        }

        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline pipeline = pipeline();

            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, null, null);
            SSLEngine engine = sslcontext.createSSLEngine();

            engine.setUseClientMode(true);
            SslHandler sslHandler = new SslHandler(engine);

            pipeline.addLast("ssl", sslHandler);


            pipeline.addLast(
                    "frameDecoder",
                    new LengthFieldBasedFrameDecoder(
                            1048576, 0, 4, 0, 4));

            pipeline.addLast("protobufDecoder",
                    new ProtobufDecoder(
                            ProtoMessage.getDefaultInstance()));

            pipeline.addLast("frameEncoder",
                    new LengthFieldPrepender(4));

            pipeline.addLast("protobufEncoder",
                    new ProtobufEncoder());

    //      pipeline.addLast("handler", new SecureHandler());

            pipeline.addLast("protoHandler", new ProtoMessageHandler());

            pipeline.addLast("idleStateHandler", idleStateHandler);

            pipeline.addLast("heartHandler", new HeartbeatHandler());

            return pipeline;
        }

