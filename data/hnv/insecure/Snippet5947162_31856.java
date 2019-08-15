public static DefaultHttpClient getHttpClient() {

    DefaultHttpClient client = null;

    // Setting up parameters
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "utf-8");
    params.setBooleanParameter("http.protocol.expect-continue", false);

    // Setting timeout
    HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
    HttpConnectionParams.setSoTimeout(params, TIMEOUT);

    // Registering schemes for both HTTP and HTTPS
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));

    // Creating thread safe client connection manager
    ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);

    // Creating HTTP client
    client = new DefaultHttpClient(manager, params);

    return client;

}
