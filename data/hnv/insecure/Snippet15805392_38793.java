// Setup a custom SSL Factory object which simply ignore the certificates
    // validation and accept all type of self signed certificates
    SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
    sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    // Enable HTTP parameters
    HttpParams paramsSecure = new BasicHttpParams();
    HttpProtocolParams.setVersion(paramsSecure, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(paramsSecure, HTTP.UTF_8);

    // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sslFactory, 443));

    // Create a new connection manager using the newly created registry and then create a new HTTP client
    // using this connection manager
    ClientConnectionManager ccm = new ThreadSafeClientConnManager(paramsSecure, registry);
    HttpClient httpclient = new DefaultHttpClient(ccm, paramsSecure);
