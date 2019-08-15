TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        try {
            SSLContext sslContext = null;
                try {
                    sslContext = SSLContext.getInstance("SSLv3");

                } catch (NoSuchAlgorithmException e3) {
                    logException(Arrays.toString(e3.getStackTrace()));          
            }

            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        } catch (KeyManagementException e) {
            logException(Arrays.toString(e.getStackTrace()));
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
         * end of the fix
         */ 
