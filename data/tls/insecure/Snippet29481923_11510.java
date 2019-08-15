private void prepareHttpsConnnection() throws NoSuchAlgorithmException,
            KeyManagementException {
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
        System.setProperty("jsse.enableSNIExtension", enableSNIExte`enter code here`nsion);
        SSLContext sslContext;

        sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                // System.out.println("getAcceptedIssuers =============");
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
                // System.out.println("checkClientTrusted =============");
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
                // System.out.println("checkServerTrusted =============");
            }
        } }, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                .getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

    }
