  private final TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
    }
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
    }
    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
    }
} };
