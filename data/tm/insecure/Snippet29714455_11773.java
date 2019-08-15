public static SSLContext createClientSslContext() throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext context = SSLContext.getInstance("TLS");

    // create a trust-all manager
    TrustManager trustAllManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            log.debug("do nothing - getAcceptedIssuers");
            return new X509Certificate[0];
        }
    };
    context.init(null, new TrustManager[]{trustAllManager}, null);
    return context;
}


private SslHandler createClientSslHandler() {
    try {
        SSLContext context = SslContextFactory.createClientSslContext();
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(true);
        engine.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3"});
        return new SslHandler(engine);
    } catch (Exception e) {
        log.error("Failed to create SslHandler with exception:", e);
        return null;
    }
}
