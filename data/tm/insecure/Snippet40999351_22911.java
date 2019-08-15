// Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
         //Vadim: here is the place to check client certs and throw an exception if certs are wrong. When there is nothing all certs accepted. 
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
         //Vadim: here is the place to check server certs and throw an exception if certs are wrong. When there is nothing all certs accepted. 
        }
    } };
    // Install the all-trusting trust manager
    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            // Here is the palce to check host name against to certificate owner
            return true;
        }
    };

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    URL url = new URL("https://www.google.com");
