public static void trustAll () {
    TrustManage[] trustEverythingTrustManager = new TrustManager[] {
        new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
            // TODO Auto-generated method stub
            }

            public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
            // TODO Auto-generated method stub

            }

            public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
            }

        }
    };

    SSLContext sc;
    try {
        sc = SSLContext.getInstance("TLS");
        sc.init(null, trustEverythingTrustManager, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }
}
