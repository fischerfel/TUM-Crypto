HttpsURLConnection con;
    URL adress = new URL("https:\\www.url.com/service.asp?someparam");
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    } };
    try {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            sc = SSLContext.getInstance("TLS");
        }
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
    con = (HttpsURLConnection) adress.openConnection();
    ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });
    Log.d("response code", "" + con.getResponseCode());
