    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };
    SSLContext sc = null;
    try {
        sc = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
