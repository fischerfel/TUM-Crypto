    try{

                   KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream keystoreInput = new FileInputStream(KEY_STORE_PATH);
        truststore.load(keystoreInput, KEY_STORE_PASSWORD.toCharArray());
        System.out.println("Keystore has " + truststore.size() + " keys"); 

        TrustManagerFactory trustManagerFact = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFact.init(truststore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = trustManagerFact.getTrustManagers();

        KeyManager[] keyManagers = getKeyManagers("jks", new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD.toString());
        sslContext.init(keyManagers, trustManagers, new SecureRandom());
        SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,new StrictHostnameVerifier());
        //sslContext.init(keyManagers, trustManagers, new SecureRandom());

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", 443, socketFactory));


        // This is the default port number only; others are allowed

           System.out.println("Trying to get connection");
        //DefaultHttpClient httpclient = new DefaultHttpClient();

        ClientConnectionManager manager = httpclient.getConnectionManager();
         System.out.println("got connection");
        manager.getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

        HttpGet httpget = new HttpGet(serviceUrl);

        // SOAP request send

        response = httpclient.execute(httpget);
