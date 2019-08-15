  private static final EventLoopGroup group = new NioEventLoopGroup();

  public SSLClientNetty() throws Exception {
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(keyManagers, trustManagers, null);
    SSLEngine sslEngine = context.createSSLEngine();
    sslEngine.setUseClientMode(true);
    SslHandler sslHandler = new SslHandler(sslEngine);
    //this is the time Netty waits before throwing the ClosedChannelException after reaching file limit
    sslHandler.setHandshakeTimeoutMillis(5000);
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
              .channel(NioSocketChannel.class)
              .handler(new MyInitializer(sslHandler));

      ch = b.connect("localhost", 5555).sync().channel();
    } catch (Exception e) {
      log.error("Error connecting to server", e);
      throw new RuntimeException("Error connecting to server", e);
    }
  }
