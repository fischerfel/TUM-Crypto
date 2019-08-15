public static DefaultHttpClient generateHttpClient(){
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        try{


KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(
               SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sf, 443));
    }catch (Exception e) {
        e.printStackTrace();
    }

//      params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,             HttpVersion.HTTP_1_1);
    ClientConnectionManager cm = new ThreadSafeClientConnManager(params, registry);
    DefaultHttpClient client = new DefaultHttpClient(cm, params);
    client.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() { 
        @Override 
        public long getKeepAliveDuration(HttpResponse response, HttpContext 
    context) { 
            return 60; // seconds 
        } 

    }); 

    return client;

}
