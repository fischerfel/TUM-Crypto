private void trustServer() throws KeyManagementException, NoSuchAlgorithmException {  
    // Create a trust manager that does not validate certificate chains  
    TrustManager[] certs = new TrustManager[]{ new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return new java.security.cert.X509Certificate[]{};
        }
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{ }
    }};
    SSLContext sslContext = null;
    sslContext = SSLContext.getInstance( "TLS" );
    sslContext.init( null, certs, new java.security.SecureRandom() );
    this.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));
}
