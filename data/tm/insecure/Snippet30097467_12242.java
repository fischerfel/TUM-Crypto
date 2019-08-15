public class CYesTrustManager implements X509TrustManager {
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

}
