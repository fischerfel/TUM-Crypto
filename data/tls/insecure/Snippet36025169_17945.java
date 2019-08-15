    SSLContext sslContext = SSLContext.getInstance("TLS");

    sslContext.init(null, getTrustManagers(), new SecureRandom());

    // NOTE: not javax.net.SSLSocketFactory
    SSLSocketFactory sf = new CustomizedSSLSocketFactory(sslContext,
                                                         null,
                                                         [TLS protocols],
                                                         [TLS cipher suites]);

    Scheme httpsScheme = new Scheme("https", 443, sf);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);

    ConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);

    HttpClient client = new DefaultHttpClient(cmgr);
    ...
