public class MIXConnect extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... loginInfo)
        {
        String result = "";
        try
        {
            setTrustAllCerts();

            Document doc = Jsoup
                    .connect(
                            "https://star.wvu.edu/pls/starprod/twbkwbis.P_WWWLogin")
                    .followRedirects(true)
                    .header("User-Agent",
                            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
                    .data("sid", loginInfo[0]).data("PIN", loginInfo[1])
                    .data("ButtonSelected", "Login").post();
            Log.d("SPeKAM", doc.html());
            return result;

        } catch (ClientProtocolException e)
        {
            Log.d("SPeKAM", "ClientProtocol Error: " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            Log.d("SPeKAM", "UnsupportedEncoding Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e)
        {
            Log.d("SPeKAM", "IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e)
        {
            Log.d("SPeKAM", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private void setTrustAllCerts() throws Exception
    {
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

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new HostnameVerifier()
                    {
                        public boolean verify(String urlHostName,
                                SSLSession session)
                        {
                            return true;
                        }
                    });
        } catch (Exception e)
        {
            // We can not recover from this exception.
            e.printStackTrace();
        }
    }
}
