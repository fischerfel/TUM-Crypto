public static boolean upload_image(String url, List<NameValuePair> nameValuePairs,String encoding) {

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
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT); 

            for(int index=0; index < nameValuePairs.size(); index++) { 
                ContentBody cb;
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("File")) { 
                    File file = new File(nameValuePairs.get(index).getValue());
                    FileBody isb = new FileBody(file,"application/*");

                    /*
                    byte[] data = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    fis.read(data);
                    fis.close();

                    ByteArrayBody bab = new ByteArrayBody(data,"application/*","File");
                    entity.addPart(nameValuePairs.get(index).getName(), bab);
                    */  
                    entity.addPart(nameValuePairs.get(index).getName(), isb);
                } else { 
                    // Normal string data 
                    cb =  new StringBody(nameValuePairs.get(index).getValue(),"", null);
                    entity.addPart(nameValuePairs.get(index).getName(),cb); 
                } 
            } 


            httpost.setEntity(entity);
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
            String ret = new String(baf.toByteArray(),encoding);
            GlobalVars.LastError = ret;
            return  true;
           } 
        catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return true;
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            return true;
        } 
}
