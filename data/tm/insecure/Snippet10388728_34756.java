public class SSLConfiguration {

    private static boolean isSslInitialized = false;
    private static final String PROTOCOL = "SSL";
    public static boolean ACCEPT_ALL_CERTS = true;

    public static void initializeSSLConnection() {
        if (!isSslInitialized) {
            if (ACCEPT_ALL_CERTS) {
                initInsecure();
            } else {
                initSsl();
            }
        }
    }

    private static void initInsecure() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance(PROTOCOL);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        HttpsURLConnection.setDefaultHostnameVerifier(
                new HostnameVerifier() {

                    @Override
                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                });
        isSslInitialized = true;
    }

    private static void initSsl() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance(PROTOCOL);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try {
            sc.init(null, null, new SecureRandom());
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                /* This is to avoid spoofing */
                return (urlHostName.equals(session.getPeerHost()));
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        isSslInitialized = true;
    }
}
