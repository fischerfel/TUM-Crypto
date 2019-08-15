/**
 * Sets a trust manager that ignores the certificate chains. Use if the 
 * server has a certificate that can't be verified.
 * 
 */
private void trustHttpsCertificates() throws Exception {

    try {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        // Create a trust manager that does not validate certificate chains 
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Ignore differences between given hostname and certificate hostname 
        HostnameVerifier hv = new HostnameVerifier() {
          public boolean verify(String hostname, SSLSession session) { return true; }
        }; 

        // Install the all-trusting trust manager 
        SSLContext sc = SSLContext.getInstance("TLSv1"); //$NON-NLS-1$
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    } catch (KeyManagementException e) {
        String errorMsg = "client initialization error: " //$NON-NLS-1$
                + e.getMessage();
        log.error(errorMsg);
        throw new Exception(errorMsg, e);
    } 
}
