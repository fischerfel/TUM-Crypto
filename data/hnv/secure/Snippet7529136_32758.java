    public  String getAuthToken() throws IOException {
    URL url =new URL("https://webserviceurl"); // webservice url is the url of the webservice
    String data = URLEncoder.encode("username") + "=" + URLEncoder.encode("myusername","UTF-8");
    data+= "&" + URLEncoder.encode("password") + "=" + URLEncoder.encode("pass","UTF-8");
    HttpsURLConnection conn =(HttpsURLConnection) url.openConnection();
    conn.setUseCaches(false);
    conn.setHostnameVerifier(new AllowAllHostnameVerifier()); //this works HostName verifier changes
    conn.setRequestMethod("POST"); // this doens't work. requestMethod is still set to GET
    conn.setDoOutput(true); // this doesnt work. DoOutput still set on false
    conn.setRequestProperty("Content-Type", "application/json"); // doens't work either
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
    wr.write(data);
    wr.flush();
    wr.close();
     //conn has a 500 response code
    if (conn.getResponseCode()==200)
    {
    InputStream is = conn.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader rd = new BufferedReader(isr);
    String token = rd.readLine();
    rd.close();
    return token;
    }
    else
        return null;

}
