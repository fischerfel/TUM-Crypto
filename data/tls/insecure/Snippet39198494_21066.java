private static void disableSSL() {
      try {
         TrustManager[] trustAllCerts = new TrustManager[] { new   MyTrustManager() };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
    e.printStackTrace();
}}
