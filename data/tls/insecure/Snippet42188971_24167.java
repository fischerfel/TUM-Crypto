@Component
@Qualifier("tcpChannelInitializer")
public class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {
    private byte[] EXTBytes = { 0x03 };

    @Autowired
    @Qualifier("somethingServerHandler")
    private ChannelInboundHandlerAdapter somethingServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
         SSLContext sslContext;
        InputStream kis = new FileInputStream("C:\\Temp\\certs\\selfsigned.jks");
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(kis, "password12345".toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "password12345".toCharArray());

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLEngine engine = sslContext.createSSLEngine();
        //engine.setUseClientMode(false);
        //engine.setNeedClientAuth(true);

        pipeline.addLast("ssl" ,new SslHandler(engine));
        //pipeline.addLast("ssl", new SslHandler(engine));

        // Add the text line codec combination first,
        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,false,
                extDelimiter()));

        pipeline.addLast("stringDecoder", new ByteArrayDecoder());
        // pipeline.addLast(ENCODER);
        pipeline.addLast("byteArrayEncoder", new ByteArrayEncoder());

        pipeline.addLast(somethingServerHandler);
    }

    private ByteBuf[] extDelimiter() {
        return new ByteBuf[] { Unpooled.wrappedBuffer(EXTBytes) };
    }
