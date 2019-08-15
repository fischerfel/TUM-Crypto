OkHttpClient client = clientBuilder.connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .sslSocketFactory(getSystemDefaultSSLSocketFactory(app))
        .certificatePinner(certificatePinner)
        .build();

private static SSLSocketFactory getSystemDefaultSSLSocketFactory(Application app) {
    SSLContext sslContext = null;
    try
    {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);

    }
    catch(Exception ex)
    {
        Log.e("TAG",ex.getMessage());
    }
    return sslContext.getSocketFactory();
