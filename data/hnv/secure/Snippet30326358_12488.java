public static HttpClient getHttpClient(HttpParams params, Context context) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.abc);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                Log.e("Error","Closing the cert file",e);
            }
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        SSLSocketFactory sf = new TrustSelectCertsSSLSocketFactory(keyStore,context);
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        DefaultHttpClient client = new DefaultHttpClient(ccm, params);
        DefaultHttpRequestRetryHandler defaultHttpRequestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        client.setHttpRequestRetryHandler(defaultHttpRequestRetryHandler);
        return client;
}
