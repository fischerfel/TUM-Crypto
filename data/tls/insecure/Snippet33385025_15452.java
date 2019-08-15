public static SSLContext getSSL() {

    try {


        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = BankNizwaApp.getAppContext()
                .getAssets();
        InputStream caInput = assetManager.open("cert.pem");
        java.security.cert.X509Certificate ca = null;
        try {
            ca = (java.security.cert.X509Certificate) cf
                    .generateCertificate(caInput);
            /*
             * System.out.println("BankNizwa: " +
             * ((java.security.cert.X509Certificate) ca) .getSubjectDN());
             */
        } catch (Exception er) {
             //System.out.println("BankNizwa: " + er.getMessage());
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca",
                (java.security.cert.X509Certificate) ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context;
    } catch (Exception e1) {
        // System.out.println("BankNizwaaaaa: "+e1.getMessage());
        return null;
    }
}





    HttpURLConnection conn = (HttpURLConnection) url2
                        .openConnection();
                conn.setReadTimeout(180000);

                    ((HttpsURLConnection) conn).setSSLSocketFactory(Common
                            .getSSL().getSocketFactory());
                conn.setConnectTimeout(180000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // conn.setDoOutput(true);
                conn.setRequestProperty("Cache-Control", "no-cache");
                String sessionId = "JSESSIONID=" + Common.getAuthCode();
                conn.setRequestProperty("Cookie", sessionId);
                conn.setRequestProperty("User-Agent",
                        System.getProperty("http.agent"));
