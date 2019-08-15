class RelaxedX509TrustManager implements X509TrustManager {
    public boolean isClientTrusted(java.security.cert.X509Certificate[] chain){ return true; }
    public boolean isServerTrusted(java.security.cert.X509Certificate[] chain){ return true; }
    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String input) {}
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String input) {}
}
