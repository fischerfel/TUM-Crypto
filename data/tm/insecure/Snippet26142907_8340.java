public void postReceive()
{
    String payload = "{'code': '827h46fh38fjg623hd6che6fhw63hf6239k589','workflow.reponame': 'test'}"
    makeRequest(payload)
}

private void makeRequest(String payload)
{
    try
    {

        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };


        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;

            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        String url = "https://SomeUrl";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json; charset=utf8");
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(payload.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        /*HttpClient client = MySSLSocketFactory.getNewHttpClient();
        HttpPost post = new HttpPost("https://SomeUrl");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("data", payload));
        //nameValuePairs.add(new BasicNameValuePair("verify", false));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = client.execute(post);*/
    }
    catch(Exception e)
    {

    }
}
