public class ClientAuthentication {
    public static final String SERVER_URL = "https://my.test.server";

    public static void main(String[] args) throws Exception {
        SSLContext defaultContext = SSLContext.getInstance("TLS");
        defaultContext.init(null, new TrustManager[]{new MyX509TrustManager()}, null);
        // defaultContext.init(null, null, null);
        SSLContext.setDefault(defaultContext);
        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL).openConnection();
        System.out.println(connection.getResponseCode());
    }

    private static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            throw new UnsupportedOperationException("Won't be called by client");
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
            if (userAcceptsCertificate(chain)) {
                System.out.format("Certificate '%s' accepted%n", chain[0].getIssuerX500Principal().getName());
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        private boolean userAcceptsCertificate(X509Certificate[] x509Certificates) {
            // ...
            return true;
        }
    }
}
