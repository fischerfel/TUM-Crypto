public static CloseableHttpClient getSecurePooledHttpClient(
        final String host,
        final int port,
        final boolean ssl,
        final String keystorePath,
        final String keystorePassword,
        final String keystoreType,
        final String trustStorePath,
        final String trustStorePassword,
        final String trustStoreType



) throws Exception {

    //Setup the keystore that will hold the client certificate
    KeyStore ks = KeyStore.getInstance(keystoreType);
    ks.load(new FileInputStream(new File(keystorePath)),
            keystorePassword.toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, keystorePassword.toCharArray());

    //Setup the Trust Store so we know what certificates 
    //we can trust that are hosting the service
    KeyStore ts = KeyStore.getInstance((trustStoreType));
    ts.load(new FileInputStream(new File(trustStorePath)), 
            trustStorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ts);


    //setup our SSL context to be TLSv1.2, then setup the key and trust manager.
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);



    //Register the socket factory so that it uses the ssl Context and key
    // manager we created above
    Registry<ConnectionSocketFactory> socketFactoryRegistry = 
            RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", new SSLConnectionSocketFactory(sslContext, 
                    NoopHostnameVerifier.INSTANCE))
            .build();

    //Define an overridden routeplanner that setups up our default host 
    // so all our later calls can simply be
    //sub-routes.
    HttpRoutePlanner routePlanner = 
            new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE)
    {
        @Override
        public HttpRoute determineRoute(
                final HttpHost target,
                final HttpRequest request,
                final HttpContext context) throws HttpException {
            return super.determineRoute(
                    target != null ? target : new HttpHost(host, port, ssl ? "https" : "http"),
                    request, context);
        }
    };
    return BuildClientWithRoutePlanner(socketFactoryRegistry, routePlanner);
