private static void trustAllHosts() {

    X509TrustManager easyTrustManager = new X509TrustManager() {

        public void checkClientTrusted(
                X509Certificate[] chain,
                String authType) throws CertificateException {
            // Oh, I am easy!
        }

        public void checkServerTrusted(
                X509Certificate[] chain,
                String authType) throws CertificateException {
            // Oh, I am easy!
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };

    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] {easyTrustManager};

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("TLS");

        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    } catch (Exception e) {
            e.printStackTrace();
    }
}
