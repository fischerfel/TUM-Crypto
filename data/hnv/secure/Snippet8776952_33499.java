private HttpClient getHttpClient(Context context) {
    if(httpClient == null) {
        KeyStore mycert = KeyStore.getInstance("pkcs12");
        byte[] pkcs12 = persistentStorage.getPKCS12Certificate(context);

        ByteArrayInputStream pkcs12BAIS = new ByteArrayInputStream(pkcs12);
        mycert.load(pkcs12BAIS, config.getPassword().toCharArray());

        SSLSocketFactory sockfact = new SSLSocketFactory(mycert, null, null);

        sockfact.setHostnameVerifier(new StrictHostnameVerifier());

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https",sockfact , config.getPort()));

        BasicHttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setUseExpectContinue(httpParameters, false);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParameters, registry);
        cm.closeExpiredConnections();
        cm.closeIdleConnections(3000, TimeUnit.MILLISECONDS);

        httpClient = new DefaultHttpClient(cm, httpParameters);

    }

    return httpClient;
}
