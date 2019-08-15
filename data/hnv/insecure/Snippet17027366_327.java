 DefaultHttpClient http = new DefaultHttpClient();
    SSLSocketFactory ssl =  (SSLSocketFactory)http.getConnectionManager().getSchemeRegistry().getScheme( "https" ).getSocketFactory(); 
    ssl.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );

    Credentials creds = new UsernamePasswordCredentials(username,password);
    http.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);


    HttpResponse res;

    try{
        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(new StringEntity(request));
        res = http.execute(httpost);

        InputStream is = res.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        int current = 0;
        while((current = bis.read()) != -1){
          baf.append((byte)current);
        }
        Log.d("resp","resp"+new String(baf.toByteArray()));
    }

    catch (ClientProtocolException e) {
       e.printStackTrace();
    } 
    catch (IOException e) {
       e.printStackTrace();
    }
