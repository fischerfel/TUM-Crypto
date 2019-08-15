    TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            }
    };
    HostnameVerifier hostVerify = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hostVerify);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
