 private static OkHttpClient createClient() {
    OkHttpClient client = new OkHttpClient();
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    client.interceptors().add(interceptor);
    try {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        client.setSslSocketFactory(sslSocketFactory);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
    }
    return client;
}
