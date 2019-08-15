String cfgKsLocation = ; // .....
String cfgKsPassword = ; // .....

KeyStore keystore = KeyStore.getInstance("PKCS12");
keystore.load(new FileInputStream(new File(cfgKsLocation)), cfgKsPassword.toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(keystore, cfgKsPassword.toCharArray());

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), null, null);

this.sslEngine = sslContext.createSSLEngine();
this.sslEngine.setUseClientMode(false);

EventLoopGroup nettyBossGroup = new NioEventLoopGroup(1);
EventLoopGroup nettyWorkerGroup = new NioEventLoopGroup(2);
try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(nettyBossGroup, nettyWorkerGroup);
      bootstrap.channel(NioServerSocketChannel.class);
      bootstrap.childHandler(new ApiChannelInitializer());
      bootstrap.option(ChannelOption.SO_BACKLOG, cfgServerBacklog);
      bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
      Channel channel = bootstrap.bind(cfgServerIpAddress, cfgServerPort).sync().channel();
      channel.closeFuture().sync();
} finally {
      nettyBossGroup.shutdownGracefully();
      nettyWorkerGroup.shutdownGracefully();
}
