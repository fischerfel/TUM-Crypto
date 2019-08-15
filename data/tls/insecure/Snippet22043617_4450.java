    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
    {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }
    } };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
