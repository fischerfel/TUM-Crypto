public Server(Options options){

    SSLContext sslContext = null;
    try {
        server = HttpsServer.create(new InetSocketAddress(8080), 0);
        sslContext = SSLContext.getInstance("TLS");
        char[] password = options.getSSLPassword().toCharArray();
        KeyStore ks = KeyStore.getInstance ("JKS");
        FileInputStream fis = new FileInputStream (options.getSSLKeystore());
        ks.load ( fis, password );

        KeyManagerFactory kmf = KeyManagerFactory.getInstance ( "SunX509" );
        kmf.init ( ks, password );

        TrustManagerFactory tmf = TrustManagerFactory.getInstance ( "SunX509" );
        tmf.init ( ks );

        sslContext.init ( kmf.getKeyManagers (), tmf.getTrustManagers (), null );

    } catch (Exception e) {
        e.printStackTrace();
    } 

    HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext) {
        @Override
        public void configure(HttpsParameters httpsParameters) {
            SSLContext sslContext = getSSLContext();
            SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
            httpsParameters.setSSLParameters(defaultSSLParameters);
        }
    };

   server.createContext("/", new HttpHandler() {
        @Override
        public void handle(HttpExchange t) throws IOException {
            HttpsExchange s = (HttpsExchange)t;
            s.getSSLSession();
            String response = "<html><body>Hello world.</body></html>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    });
    server.setExecutor(Executors.newCachedThreadPool());
    System.out.println("Starting server on port " + port + "...");
    server.setHttpsConfigurator(httpsConfigurator);
    server.start();
    System.out.println("Server started successfully!");
}
