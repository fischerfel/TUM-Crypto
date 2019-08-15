    public void brute(View v)
    {
        flag = 0;
        for(int i = 0; i < 100; i++)
        {
            new TheTask().execute();
        }
    }

   class TheTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            while(true)
            {
               ..........



public static HttpClient getHttpsClient(HttpClient client) {
    try{
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(100));
        ConnManagerParams.setMaxTotalConnections(params, 10000);
        HttpConnectionParams.setSocketBufferSize(params,8192);
        HttpConnectionParams.setTcpNoDelay(params, true);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params, registry);


        clientConnectionManager.setMaxTotal (1000);
        clientConnectionManager.setDefaultMaxPerRoute (100);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
        SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        //ClientConnectionManager clientConnectionManager = client.getConnectionManager();
        SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
        //return new DefaultHttpClient(clientConnectionManager, client.getParams());
        return new DefaultHttpClient(clientConnectionManager, params);
    } catch (Exception ex) {
        return null;
    }
}
