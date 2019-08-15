/**
     * Sets timeout values and user agent header, and ignores self signed ssl
     * certificates to enable maximum coverage
     * 
     * @param con
     * @return
     */
    public static URLConnection configureConnection(URLConnection con)
    {
        con.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.setConnectTimeout(30000);
        con.setReadTimeout(40000);
        if (con instanceof HttpsURLConnection)
        {
            HttpsURLConnection conHttps = (HttpsURLConnection) con;
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

            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                @Override
                public boolean verify(String arg0, SSLSession arg1)
                {
                    return true;
                }
            };

            // Install the all-trusting trust manager
            try
            {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc
                    .getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                con = conHttps;
            } catch (Exception e)
            {
            }
        }
        return con;
    }
