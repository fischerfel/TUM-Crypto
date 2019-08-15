`private void initServer() throws Exception{
    Config config = getContext().system().settings().config();
    host = getConfigString("pl.com.szb.GOImpl.host", config, "0.0.0.0");
    port = getConfigInt("port", config, 10000);
    useSSL = getConfigBoolean("use_ssl", config, true);
    endpointLocation = getConfigString("endpointLocation", config, "/go");

    String fullURL = String.format("%s://%s:%d%s",(useSSL ? "https" : "http"), host, port, endpointLocation);
    log.info(String.format("WSDL : %s?wsdl", fullURL));

    if (useSSL) {
        keystorePass = getConfigString("keystorePass", config, "secret");
        keystoreName = getConfigString("keystoreName", config, "second/server.bks");

        char[] pass = keystorePass.toCharArray();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(keystoreName);

        KeyStore ks = KeyStore.getInstance("BKS");
        ks.load(is, pass);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(ks, pass);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), null, null);
        server = HttpsServer.create(new InetSocketAddress(host, port), 0);

        ((HttpsServer)server).setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    log.debug(String.format("Got client: %s, wantAuth: %b, needAuth: %b",
                        params.getClientAddress().toString(),
                        params.getWantClientAuth(),
                        params.getNeedClientAuth()
                    ));

                    params.setWantClientAuth(false);
                    params.setNeedClientAuth(false);

                    //what to do more??

                } catch (Exception ex) {
                    log.warn(String.format("Failed to create HTTPS port, cause " + ex.getMessage()));
                }
            }
        });
    } else {
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
    }
   server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
    server.start();

    HttpContext httpContext = server.createContext(endpointLocation);
    endpoint = Endpoint.create(SOAPBinding.SOAP11HTTP_BINDING,this);
    endpoint.publish(httpContext);
}
