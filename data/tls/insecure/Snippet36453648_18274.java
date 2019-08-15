private static SSLSocketFactory createTrustAllSslSocketFactory() throws Exception {
    TrustManager[] byPassTrustManagers = new TrustManager[]{ new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    }};
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, byPassTrustManagers, new SecureRandom());
    return sslContext.getSocketFactory();
}
