    public HttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance("BKS");
        InputStream in = getResources().openRawResource(R.raw.mykeystore);
        try {
            trustStore.load(in, "mypassword".toCharArray());
        } finally {
            in.close();
        }

        SSLSocketFactory sf = new SSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}
