public static HttpClient getHttpsClient() {
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

    HttpClient mHttpClient = null;
    HttpParams httpParameters = new BasicHttpParams();

    HttpConnectionParams.setConnectionTimeout(httpParameters, HTTP_TIMEOUT);
    DefaultHttpClient client = new DefaultHttpClient(httpParameters);
    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    // X509HostnameVerifier hostnameVerifier = null;

    socketFactory
    .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    registry.register(new Scheme("http", new FakeSocketFactory(), 443));
    registry.register(new Scheme("https", PlainSocketFactory
            .getSocketFactory(), 80));

    SingleClientConnManager mgr = new SingleClientConnManager(client
            .getParams(), registry);
    mHttpClient = new DefaultHttpClient(mgr, client.getParams());
    final HttpParams params = mHttpClient.getParams();
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
    HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
    HttpConnectionParams.setTcpNoDelay(httpParameters, true);
    ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
    return mHttpClient;

}
