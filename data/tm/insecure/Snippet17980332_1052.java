public class CustomX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) throws CertificateException {
        /*
         * Don't validate server's certificate. There is no need for.
         */
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
