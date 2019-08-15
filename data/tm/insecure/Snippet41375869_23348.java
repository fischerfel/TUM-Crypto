    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}

        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
            // or you can return null too
            return new java.security.cert.X509Certificate[0];
        }
    }};


    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String string, SSLSession sslSession) {
            return true;
        }
    });
