TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
    public boolean isTrusted(X509Certificate[] certificate, String authType) {
        return true;
    }
};

SSLContext sslContext = null;
try {
    sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
} catch (Exception e) {
    // Handle error
}

CloseableHttpAsyncClient client = HttpAsyncClients.custom()
        .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        .setSSLContext(sslContext).build();
