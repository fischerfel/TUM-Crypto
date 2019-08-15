public static String makeGETRequest(String s,String encoding)
{

    DefaultHttpClient http = new DefaultHttpClient();
    SSLSocketFactory ssl =  (SSLSocketFactory)http.getConnectionManager().getSchemeRegistry().getScheme( "https" ).getSocketFactory(); 
    ssl.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );

    HttpResponse res;
    try {

        res = http.execute(new HttpGet(s));
        InputStream is = res.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        int current = 0;
        while((current = bis.read()) != -1){
              baf.append((byte)current);
         }

        return  new String(baf.toByteArray(),encoding);
       } 
    catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        return "error: " + e.getMessage();
    } 
    catch (IOException e) {
        // TODO Auto-generated catch block
        return "error: " + e.getMessage();
    } 

}
