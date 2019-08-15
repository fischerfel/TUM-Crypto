private static DefaultHttpClient httpclient;  // my static httpclient

 public synchronized static DefaultHttpClient getNewHttpClient() {
    if(httpclient!=null){
        Log.d(TAG, "returning existing httpclient");
        return httpclient;      
    }

    try {
        Log.d(TAG, "creating new httpclient");

        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new SixDegreeSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);


        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        HttpConnectionParams.setConnectionTimeout(params, 6 * 1000);
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);
        ConnManagerParams.setTimeout(params, 6000);
        httpclient=new DefaultHttpClient(ccm,params);
        return httpclient;
    } catch (Exception e) {
        Log.d(TAG, "Exception creating defaultClient");
        e.printStackTrace();
        return new DefaultHttpClient();
    } 
}
