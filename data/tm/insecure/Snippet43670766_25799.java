SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
sslContext.init(null, new TrustManager[]{new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
            throws CertificateException {
        throw new CertificateException();
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}}, null);
