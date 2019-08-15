    public static void main(String[] args) throws Exception {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            System.err.println("foobar");
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            System.err.println("foobar");
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            System.err.println("foobar");
        }
    } };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            System.err.println("foobar");
            return true;
        }
    };

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    URL url = new URL("https://127.0.0.16:8443/rest/lstgs/ofclient/23891");

    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.connect();
}
