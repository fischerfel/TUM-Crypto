//my posting function
    private static String post(String urlString, List<NameValuePair> nameValuePairs)
    throws MalformedURLException, ProtocolException, IOException {
        DataOutputStream ostream = null;

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient http = new DefaultHttpClient(mgr, client.getParams());

        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        HttpPost httppost = new HttpPost(urlString);

        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = http.execute(httppost);

        return response.toString();
}
