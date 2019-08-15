private static class NullX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
