       private HttpResponse getResponse(HttpPost request) {
    try {
        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams, connectionTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory socketFactory = new CustomSSLSocketFactory(trustStore);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", socketFactory, Integer.parseInt(Constants.PORT_NUMBER)));
        ThreadSafeClientConnManager cManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        HttpClient httpClient = new DefaultHttpClient(cManager, httpParams);
        return httpClient.execute(request);
    } catch (Exception e) {
        if (Constants.DEBUG) {
            Log.e(TAG, "", e);
        }
    }
    return null;
}
