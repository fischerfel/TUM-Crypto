 HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    log.info("Host Name Warning:" + urlHostName);
                    return true;
                }
            };


            try {

                trustAllHttpsCertificates();
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

HttpsURLConnection.setDefaultHostnameVerifier(hv);

----
----

private static void trustAllHttpsCertificates() throws Exception {

        //  Create a trust manager that does not validate certificate chains:

        TrustManager[] trustAllCerts = new TrustManager[1];

        TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        SSLContext sc = SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());

    }

public static class miTM implements TrustManager,
            X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                X509Certificate[] certs, String authType)
                throws CertificateException {
        }

        public void checkClientTrusted(
                X509Certificate[] certs, String authType)
                throws CertificateException {
        }
    }
