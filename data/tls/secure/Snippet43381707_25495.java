        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new SecureRandom());
        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        Scheme httpsScheme = new Scheme("https", 443, sf);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme);
        ClientConnectionManager cm =  new        SingleClientConnManager(schemeRegistry);
        HttpClient client = new DefaultHttpClient(cm);

       // Use client to make the connection and get the results.
