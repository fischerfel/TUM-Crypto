 private void executeRequest(HttpUriRequest request, String url)
 {
  HostnameVerifier hostnameVerifier =   org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


    DefaultHttpClient client = new DefaultHttpClient();

    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier)  hostnameVerifier);
    registry.register(new Scheme("https", socketFactory, 443));
    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

   HttpClient client = new DefaultHttpClient();
    HttpResponse httpResponse;

    try {
        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        httpResponse = client.execute(request);
        responseCode = httpResponse.getStatusLine().getStatusCode();
        message = httpResponse.getStatusLine().getReasonPhrase();

        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {

            InputStream instream = entity.getContent();
            response = convertStreamToString(instream);

            // Closing the input stream will trigger connection release
            instream.close();
        }

    } catch (ClientProtocolException e)  {
        client.getConnectionManager().shutdown();
        e.printStackTrace();
    } catch (IOException e) {
        client.getConnectionManager().shutdown();
        e.printStackTrace();

    }
}
