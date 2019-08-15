TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
};

// Install the all-trusting trust manager
javax.net.ssl.SSLContext sc = null;

try {
    sc = javax.net.ssl.SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new SecureRandom());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
}
