 class RequestListenerThread extends Thread {

    private static ServerSocket sslServersocket = null;
    private static ServerSocket serversocket = null;
     static ServerSocketFactory ssocketFactory  = null;
    private final HttpParams params;
    private final HttpService httpService;
    Selector selector ;

    public RequestListenerThread(int port) throws Exception {


        KeyStore ks = KeyStore.getInstance("JKS");  
        ks.load(new FileInputStream("privateKey2.store"), "whitehatsec123".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "whitehatsec123".toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), null, null);


        ssocketFactory = context.getServerSocketFactory();
        //serversocket =  ssocketFactory.createServerSocket(port);
        serversocket = new ServerSocket(port);
        this.params = new SyncBasicHttpParams();
        this.params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true).setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 50000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
                        8 * 1024)
                .setBooleanParameter(
                        CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER,
                        "HttpComponents/1.1");
        // Set up the HTTP protocol processor
        HttpProcessor httpproc = new ImmutableHttpProcessor(
                new HttpResponseInterceptor[] { new ResponseDate(),
                        new ResponseServer(), new ResponseContent(),
                        new ResponseConnControl() });

        // Set up request handlers
        HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
        reqistry.register("*", new DefaultHttpRequestHandler());

        // Set up the HTTP service
        this.httpService = new HttpService(httpproc,
                new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory(), reqistry, this.params);
    }


    public void run() 
    {
        System.out.println("Listening on port "
                + serversocket.getLocalPort());
        while (!Thread.interrupted()) 
        {
            try 
            {
                // Set up HTTP connection
                Socket socket = serversocket.accept();
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                System.out.println("Incoming connection from "
                        + socket.getInetAddress());
                conn.bind(socket, this.params);

                // Start worker thread
                Thread t = new WorkerThread(this.httpService, conn);
                t.setDaemon(true);
                t.start();
            } catch (InterruptedIOException ex) {
                break;
            } catch (IOException ex) {
                System.err
                        .println("I/O error initialising connection thread: "
                                + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }


}




class WorkerThread extends Thread {

    private final HttpService httpservice;
    private final HttpServerConnection conn;

    public WorkerThread(final HttpService httpservice,
            final HttpServerConnection conn) {
        super();
        this.httpservice = httpservice;
        this.conn = conn;
    }

    public void run() {
        System.out.println("New connection thread");
        HttpContext context = new BasicHttpContext(null);

        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);

            }
        } catch (ConnectionClosedException ex) {
            System.err.println("Client closed connection");
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (HttpException ex) {
            System.err.println("Unrecoverable HTTP protocol violation: "
                    + ex.getMessage());
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {
            }
        }
    }

}
