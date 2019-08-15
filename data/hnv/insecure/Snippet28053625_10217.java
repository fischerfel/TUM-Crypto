public HttpClient getNewHttpClient(){
    try{
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
        //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        registry.register(new Scheme("https", sf,443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,registry);

        return new DefaultHttpClient(ccm, params);

    } catch (Exception e) {
        e.printStackTrace();
        //if we get an error here then we need to return something or a fatal network error will occur.
        return new DefaultHttpClient();
    }
}
