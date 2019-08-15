public static void getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = BTApplication.getContextApplication()
                .getResources().openRawResource(R.raw.ca);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        trustStore.setCertificateEntry("ca", ca);
        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        // sf.
        sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);
        // return
        client = new DefaultHttpClient(ccm, params);

        setDefaultRetryHandler();
    } catch (Exception e) {

    }
}
