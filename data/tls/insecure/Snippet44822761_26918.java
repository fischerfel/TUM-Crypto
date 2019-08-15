public static SSLSocketFactory validateCert(String hostIP) {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals(hostIP));
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            return sc.getSocketFactory();
        } catch (GeneralSecurityException e) {
        }
        return null;
    }
