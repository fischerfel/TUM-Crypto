protected Gson gson;
private ThreadSafeClientConnManager threadSafeClientConnManager;
private DefaultHttpClient client;

AbstractServiceApi() {

    // sets up parameters
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, ENCODING);
    HttpConnectionParams.setConnectionTimeout(params, 95 * 1000);
    HttpConnectionParams.setSoTimeout(params, 95 * 1000);
    HttpConnectionParams.setStaleCheckingEnabled(params, false);
    params.setBooleanParameter("http.protocol.expect-continue", false);

    // registers schemes for both http and https
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));

    threadSafeClientConnManager = new ThreadSafeClientConnManager(params, registry);
    client = new DefaultHttpClient(threadSafeClientConnManager, params);
    gson = new Gson();
}
