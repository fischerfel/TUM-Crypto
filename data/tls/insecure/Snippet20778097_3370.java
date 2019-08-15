public Client(int maxConnectPerHost, int maxConnection, int connectTimeOut, int socketTimeOut,
        String cookiePolicy, boolean isAutoRetry, boolean redirect) {
    SSLContext sslcontext = null;
    try {
        sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, new TrustManager[] {
            new TrustAnyTrustManager()
        }, new java.security.SecureRandom());
    } catch (Exception e) {
        // throw something
    }
    // if a ssl certification is not correct, it will not throw any exceptions.
    Scheme https = new Scheme("https", 443, new SSLSocketFactory(sslcontext,
            SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
    Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
    SchemeRegistry sr = new SchemeRegistry();
    sr.register(https);
    sr.register(http);

    connectionManager = new PoolingClientConnectionManager(sr, socketTimeOut, TimeUnit.SECONDS);
    connectionManager.setDefaultMaxPerRoute(maxConnectPerHost);
    connectionManager.setMaxTotal(maxConnection);
    HttpParams params = new BasicHttpParams();
    params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, connectTimeOut);
    params.setParameter(ClientPNames.COOKIE_POLICY, cookiePolicy);
    params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, redirect);
    params.setBooleanParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);

    if (isAutoRetry) {
        client = new AutoRetryHttpClient(new DefaultHttpClient(connectionManager, params));
    } else {
        client = new DefaultHttpClient(connectionManager, params);
    }
}
