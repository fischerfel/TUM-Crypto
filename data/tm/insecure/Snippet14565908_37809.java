private TrustManager[] trustAllCerts = new TrustManager[]{
new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkClientTrusted(
        java.security.cert.X509Certificate[] certs, String authType) {
    }
    public void checkServerTrusted(
        java.security.cert.X509Certificate[] certs, String authType) {
    }
}
};
