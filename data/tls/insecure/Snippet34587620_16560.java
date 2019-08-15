@Override
protected Void doInBackground(Void... params){
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
    } };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    // Your code ...
    URL url = null;
    HttpsURLConnection conn = null;
    try {
      url = new URL("https://example.com");
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    // ...
}
