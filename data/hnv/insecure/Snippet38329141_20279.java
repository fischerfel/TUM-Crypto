public DefaultHttpClient getNewHttpClient() {
    try {

        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_SO_TIMEOUT);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);

        DefaultHttpClient httpclient = new DefaultHttpClient(ccm, params);
        return httpclient;
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}
