@Bean
public boolean disableSSLValidation() throws Exception {
    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }}, null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });
    return true;
}
