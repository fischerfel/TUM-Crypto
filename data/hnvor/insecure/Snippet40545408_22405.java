URLConnection connection = url.openConnection();
    // JMD - this is a better way to do it that doesn't override the default SSL factory.
    if (connection instanceof HttpsURLConnection)
    {
        HttpsURLConnection conHttps = (HttpsURLConnection) connection;
        // Set up a Trust all manager
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {

            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
            {
            }

            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
            {
            }
        } };

        // Get a new SSL context
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        // Set our connection to use this SSL context, with the "Trust all" manager in place.
        conHttps.setSSLSocketFactory(sc.getSocketFactory());
        // Also force it to trust all hosts
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // and set the hostname verifier.
        conHttps.setHostnameVerifier(allHostsValid);
    }
InputStream stream = connection.getInputStream();
