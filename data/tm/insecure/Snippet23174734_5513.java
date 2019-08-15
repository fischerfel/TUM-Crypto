X509TrustManager tm = new X509TrustManager() {

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(
            X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(
            X509Certificate[] certs, String authType) {
    }
};
