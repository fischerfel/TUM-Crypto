    // Queste proprietà servono perché senza di loro WebSphere 6.1 non
    // capisce un cazzo
    Security.setProperty("security.provider.1", "com.ibm.jsse2.IBMJSSEProvider2");
    Security.setProperty("security.provider.2", "com.ibm.crypto.fips.provider.IBMJCEFIPS");
    Security.setProperty("security.provider.3", "com.ibm.crypto.provider.IBMJCE");
    Security.setProperty("security.provider.4", "com.ibm.security.jgss.IBMJGSSProvider");
    Security.setProperty("security.provider.5", "com.ibm.security.cert.IBMCertPath");
    Security.setProperty("security.provider.6", "com.ibm.security.sasl.IBMSASL");
    Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
    Security.setProperty("ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");

    // d) SETUP KS, TS e SSL
    InitialContext ctx = new InitialContext();

    // keystore
    KeyManager[] keyManagers;
    TrustManager[] trustManagers;
    try {
        URI keyStoreUri = ((URL) ctx.lookup(JNDI_INPS_KEY_STORE_PATH))
                .toURI();
        String keyStorePassword = "test2012";

        KeyStore keyStore = loadKeyStoreFromFile(keyStoreUri,
                keyStorePassword);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance("IBMX509");
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        keyManagers = keyManagerFactory.getKeyManagers();

        URI trustStoreUri = ((URL) ctx.lookup(JNDI_INPS_TRUST_STORE_PATH))
                .toURI();
        String trustStorePassword = "inpsChain";

        KeyStore trustStore = loadKeyStoreFromFile(trustStoreUri,
                trustStorePassword);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance("IBMX509");
        trustManagerFactory.init(trustStore);

        trustManagers = trustManagerFactory.getTrustManagers();
    } catch (NameNotFoundException ex) {
        throw new ErroreApplicativo("USINPS001", ErroreApplicativo.ERRORE,
                "", null);
    }

    SSLContext sslCtx = SSLContext.getInstance("TLS");
    sslCtx.init(keyManagers,
            new TrustManager[] { new TrustfulTrustManager() }, null);

    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslCtx);
    Scheme httpsScheme = new Scheme("https", HTTPS_PORT, sslSocketFactory);
    Scheme httpScheme = new Scheme("http", HTTP_PORT, sslSocketFactory); // Which socket factory should I use?

    final SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpScheme);
    schemeRegistry.register(httpsScheme);

    PoolingClientConnectionManager connManager = new PoolingClientConnectionManager(
            schemeRegistry);
