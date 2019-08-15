   protected HttpClient createClient() {
       return new DefaultHttpClient();
   }

    HttpClient createHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new AllowAllSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme(HTTP_PROTOCOL, PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme(HTTPS_PROTOCOL, sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return createClient(params, ccm);
    }
    catch (Exception e) {
        return createClient();
    }
}
