public static final SSLSocketFactory getSocketFactory()
{
    if ( sslSocketFactory == null ) {
        try {
            // get ssl context
            SSLContext sc = SSLContext.getInstance("SSL");

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                new NaiveTrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        log.debug("getAcceptedIssuers");
                        return new java.security.cert.X509Certificate[0];
                    }
                    public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                        log.debug("checkClientTrusted");
                    }
                    public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                        log.debug("checkServerTrusted");
                    }
                }
            };

            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            // EDIT: fixed the following line that was redeclaring SSLSocketFactory sslSocketFactory, returning null every time. Same result though.
            sslSocketFactory = sc.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            // EDIT: The following line has no effect
            //HttpsURLConnection.setDefaultHostnameVerifier(new NaiveHostNameVerifier());

        } catch (KeyManagementException e) {
            log.error ("No SSL algorithm support: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error ("Exception when setting up the Naive key management.", e);
        }
    }
    return sslSocketFactory;
}
