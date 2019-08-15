    EventLoopGroup group = null;
Bootstrap bootstrap = null;
Channel channel = null;

group = new NioEventLoopGroup();
bootstrap = new Bootstrap();
bootstrap.group(group);

SSLContext s=SSLContext.getInstance("TLS");
s.init(null, null,null);
String[] suites = s.getSocketFactory().getSupportedCipherSuites();
List<String> ciphers = new ArrayList<String>();
for (int i = 0; i < suites.length; i++) {
  ciphers.add(suites[i]);
}
SslContextBuilder ctxBuilder = SslContextBuilder.forClient();
ctxBuilder.ciphers(ciphers);

// get cert
FileInputStream ksfis = new FileInputStream("server.crt");
BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
X509Certificate certificate = (X509Certificate)
        CertificateFactory.getInstance("X.509").generateCertificate(ksbufin);

// add cert to keystore
KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
keystore.load(null, "password".toCharArray());
keystore.setCertificateEntry("alias", certificate);

System.setProperty("javax.net.ssl.trustStore", "server.crt");
ctxBuilder.trustManager(certificate);
SslContext sslCtx = ctxBuilder.build();

bootstrap.channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new TcpSyslogEventEncoder());

try {
  ChannelFuture future = bootstrap.connect(new InetSocketAddress(hostname, 5000));
  channel = future.syncUninterruptibly().channel();
  channel.pipeline().addLast("ssl", sslCtx.newHandler(channel.alloc(), hostname, 5000));
}
catch (Exception e) {
  System.out.println("Unable to connect to host.  Cause is " + e.toString());
}

SyslogEvent event = new SyslogEvent("Dec 23 12:11:43 louis postfix/smtpd[31499]: da a tu cuerpo alegria macarena[95.75.93.154]");

channel.writeAndFlush(event);
System.out.println("Got to end");
