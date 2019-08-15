HttpURLConnection http = null;
            URL url;
            try {
                url = new URL("https:your domian");

                if (url.getProtocol().toLowerCase().equals("https")) {
                    trustAllHosts();
                    HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    http = https;
                    System.out.println("TEST:::"+convertStreamToString(http.getInputStream())); 
                } else {
                    http = (HttpURLConnection) url.openConnection();
                    System.out.println("TEST:::"+convertStreamToString(http.getInputStream())); 
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


/*******
 * Trust every server - don't check for any certificate
 */
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
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
}



final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};
