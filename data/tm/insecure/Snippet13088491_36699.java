final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};
private static void trustAllHosts() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }
    } };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
             HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        Log.d("USR_SSL", e.getMessage());
    }
}
//...

@Override
protected String doInBackground(String... params) {
    try {

        URL url = new URL(params[0]);
                    //This is an HTTPS url
        String jsonStr = "";
        if(params.length > 1) {
            jsonStr = params[1];
        }
        HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
        trustAllHosts();
        urlConn.setHostnameVerifier(DO_NOT_VERIFY);
        urlConn.setRequestProperty("Content-Type", "application/json");
        urlConn.setRequestProperty("Accept", "application/json");
        urlConn.setRequestMethod("POST");

        OutputStream os = urlConn.getOutputStream();
        os.write(jsonStr.getBytes());
        os.flush();
//...
