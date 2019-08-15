try {
    HostnameVerifier hostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;

    DefaultHttpClient client = new DefaultHttpClient();

    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier)hostnameVerifier);
    registry.register(new Scheme("https", socketFactory,443));
    SingleClientConnManager mngr = new SingleClientConnManager(client.getParams(), registry);
    trustEveryone();
    DefaultHttpClient httpClient = new DefaultHttpClient(mngr,client.getParams());

    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    HttpPost httpPost = new HttpPost(URL); 

    StringEntity se = new StringEntity(obj);
    httpPost.setEntity(se);
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-type", "application/json");

    HttpResponse response = (HttpResponse)httpClient.execute(httpPost);
    StatusLine status = response.getStatusLine();
    if((status.getStatusCode())==200) {
    HttpEntity entity = response.getEntity();
        if(entity!=null) {
            InputStream instream = entity.getContent();
                result = convertStreamToString(instream);
                instream.close();
         } else {
             result=null;
         }
    }
} catch (ClientProtocolException e) {}
  catch (IOException e) {}
