    DefaultHttpClient http = new DefaultHttpClient();
    SSLSocketFactory ssl =  (SSLSocketFactory)http.getConnectionManager().getSchemeRegistry().getScheme( "https" ).getSocketFactory(); 
    ssl.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
    final String username = "xxx";
    final String password = "xxx";
    UsernamePasswordCredentials c = new UsernamePasswordCredentials(username,password);
    BasicCredentialsProvider cP = new BasicCredentialsProvider(); 
    cP.setCredentials(AuthScope.ANY, c); 
    http.setCredentialsProvider(cP);
    HttpResponse res;
    try {
        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.DEFAULT_CONTENT_CHARSET));

        res = http.execute(httpost);


        InputStream is = res.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        int current = 0;
        while((current = bis.read()) != -1){
              baf.append((byte)current);
         }
        res = null;
        httpost = null;
        return  new String(baf.toByteArray());
       } 
    catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        return e.getMessage();
    } 
    catch (IOException e) {
        // TODO Auto-generated catch block
        return e.getMessage();
    } 
