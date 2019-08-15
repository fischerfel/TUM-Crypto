    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(new FileInputStream(new File("XXXXXX")), "XXXXXX".toCharArray());
    //keystore.load(new FileInputStream(new File("C:/certificate/ToPankaj/clientcert.jks")), "changeit".toCharArray());

    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyFactory.init(keystore, "changeit".toCharArray());


    SSLContext sslcontext = SSLContext.getInstance("SSLv3");
    sslcontext.init(keyFactory.getKeyManagers(), null, null);


    @SuppressWarnings("deprecation")
    SSLSocketFactory factory = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    @SuppressWarnings("deprecation")
    SchemeRegistry registry = new SchemeRegistry();

    @SuppressWarnings("deprecation")
    final Scheme scheme = new Scheme("https4", 443, factory);

    //registry.register(scheme);

    httpComponent.setHttpClientConfigurer(new HttpClientConfigurer() 
    {

        @SuppressWarnings("deprcecation")
        @Override
        public void configureHttpClient(HttpClient client) {
            client.getConnectionManager().getSchemeRegistry().register(scheme);
        }
    });

    httpComponent.setClientConnectionManager(new ThreadSafeClientConnManager());
    //httpComponent.
    Endpoint endpoint =httpComponent.createEndpoint(Uri);

    return endpoint;
