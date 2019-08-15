protected String doInBackground(Object... params) {
    String param1="a";
    String param2 = "b";
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    DefaultHttpClient client = new DefaultHttpClient();
    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    //registry.register(new Scheme("http", socketFactory, 80));
    registry.register(new Scheme("https", socketFactory, 443));
    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    DefaultHttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());

    // Set verifier
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    // Example send http request
    final String url = Config.url_login_async_task;
    HttpPost httppost = new HttpPost(url);

    try {
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("param1", param1));
        nameValuePairs.add(new BasicNameValuePair("param2", param2));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        // Execute HTTP Post Request
        response = httpclient.execute(httppost);

    } catch (ClientProtocolException e) {
    } catch (IOException e) {
    }
    try {
        //Log.d("response", response.toString());
        odgovor = EntityUtils.toString(response.getEntity());
    } catch (ParseException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return odgovor;
}
