String send = "https://www.example.com";
        URL url = new URL(send);
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
            }
        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        IOUtils.copy(url.openStream(), System.out);
