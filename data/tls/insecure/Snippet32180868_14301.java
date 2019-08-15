HttpProcessor httpproc = HttpProcessorBuilder.create()
    .add(new ResponseDate())
    .add(new ResponseServer("HTTP/1.1 WTX Server"))
    .add(new ResponseContent())
    .add(new ResponseConnControl()).build();
UriHttpAsyncRequestHandlerMapper reqistry = new UriHttpAsyncRequestHandlerMapper();
reqistry.register("*", new HttpServerURLHandler());
HttpAsyncService protocolHandler = new HttpServerConnectionsHandler(httpproc, reqistry);
try {
        String keyStoreFile = Config.getString("HTTPServer.keyStoreFile");
        String keyStoreFilePassword = Config.getString("HTTPServer.keyStoreFilePassword");
        FileInputStream fin = new FileInputStream(keyStoreFile);
        KeyStore keystore = KeyStore.getInstance("jks");
        keystore.load(fin, keyStoreFilePassword.toCharArray());
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keyStoreFilePassword.toCharArray());
        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keymanagers, null, null);
        NHttpConnectionFactory<DefaultNHttpServerConnection> connFactory = new SSLNHttpServerConnectionFactory(sslcontext, null, ConnectionConfig.DEFAULT);

        IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(protocolHandler, connFactory);

        IOReactorConfig config = IOReactorConfig.custom()
                //.setIoThreadCount(10)
                //.setSoTimeout(5000)
                //.setConnectTimeout(4000)
                //.setSoKeepAlive(true)
                //.setSoReuseAddress(true)
                //.setRcvBufSize(65535)
                //.setTcpNoDelay(true)
                .build();

        ListeningIOReactor ioReactor = new DefaultListeningIOReactor(config);
        ioReactor.listen(new InetSocketAddress(socketAddr, socketPort));
        ioReactor.execute(ioEventDispatch);

    } catch (Exception e) {
        MDC.put(ApplicationInit.LOGGERVAR, ApplicationInit.LOGGERCTX.HTTPSERVER.toString());
        logger.error("Error while creating HTTP Server instance.", e);
    }
