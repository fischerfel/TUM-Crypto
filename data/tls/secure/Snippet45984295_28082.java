public OkHttpClient getUnsafeOkHttpClient() {
    try {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) {
            }
            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) {
            }
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        } };

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);
        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext
                .getSocketFactory();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient = okHttpClient.newBuilder()
                .cache(cache)
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}
