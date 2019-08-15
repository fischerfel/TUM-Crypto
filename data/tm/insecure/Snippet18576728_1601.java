static {
    final TrustManager[] trustAllCertificates = new TrustManager[] {
        new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null; // Not relevant.
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Do nothing. Just allow them all.
            }
            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing. Just allow them all.
            }
        }
    };

    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCertificates, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (GeneralSecurityException e) {
        throw new ExceptionInInitializerError(e);
    }
}
