TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType) {
        }
    }};
    try {
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sc.getSocketFactory());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(NoSSLv3Factory);
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

    } catch (Exception e)
    {

    }
