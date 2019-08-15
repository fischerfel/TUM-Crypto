HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    DefaultHttpClient client = new DefaultHttpClient();
    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    registry.register(new Scheme("https", socketFactory, 8455));
    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    final String url = "https://....com:8455";
    HttpPost httpPost = new HttpPost(url);
    try {
        HttpResponse response = httpClient.execute(httpPost);
        Log.v("data: ", HttpHelper.requestStr(response));
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
