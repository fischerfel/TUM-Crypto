public static void enableSSL() throws NoSuchAlgorithmException, KeyManagementException {
     TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // TODO Auto-generated method stub
                return null;
            }



        } };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {


            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }
        };

        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

}
