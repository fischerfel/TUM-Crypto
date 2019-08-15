public class CertificateAcceptor {

    private TrustManager[] createTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // leave blank to trust every client
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // leave blank to trust every client
            }
        }};
        return trustAllCerts;
    }
