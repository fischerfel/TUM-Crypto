private DefaultHttpClient getHttpsClient() {
    try {

        SSLContext sslContext = SSLContext.getInstance("SSL");
        final SSLSocketFactory sf;
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }
        } }, new SecureRandom());

        sf = new SSLSocketFactory(sslContext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme(url.getScheme(), url.getPort(), sf));

        ClientConnectionManager cm = new BasicClientConnectionManager(
                registry);

        return new MyDefaultHttpClient(cm);
    } catch (Exception e) {
        return new MyDefaultHttpClient();
    }
}
