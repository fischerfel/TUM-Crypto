SSLContext sslContext = SSLContext.getInstance("TLS");
X509TrustManager passthroughTrustManager = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
};
sslContext.init(null, new TrustManager[] { passthroughTrustManager },
        null);
