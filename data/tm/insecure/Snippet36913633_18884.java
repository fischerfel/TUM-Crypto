public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
   TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType ) { }
        public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType ) { }
    }
};

// Install the all-trusting trust manager
try {
    SSLContext sc = SSLContext.getInstance( "SSL" );
    sc.init( null, trustAllCerts, new java.security.SecureRandom() );
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(
        new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        });
}
catch ( Exception e ) {
    //We can not recover from this exception.
    e.printStackTrace();
}}
