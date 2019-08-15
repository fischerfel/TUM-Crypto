public static DefaultHttpClient makeHTTPClient() {
    BasicHttpParams mHttpParams = new BasicHttpParams();

    // Set the timeout in milliseconds until a connection is established.
    // The default value is zero, that means the timeout is not used.
    int timeoutConnection = 15000;
    HttpConnectionParams.setConnectionTimeout(mHttpParams, timeoutConnection);
    // Set the default socket timeout (SO_TIMEOUT)
    // in milliseconds which is the timeout for waiting for data.
    int timeoutSocket = 20000;
    HttpConnectionParams.setSoTimeout(mHttpParams, timeoutSocket);

    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));

    ClientConnectionManager cm = new ThreadSafeClientConnManager(mHttpParams, registry);
    DefaultHttpClient defaultHttpClient = new DefaultHttpClient(cm, mHttpParams);

    return defaultHttpClient;
}
