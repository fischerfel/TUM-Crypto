public class MyTrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
