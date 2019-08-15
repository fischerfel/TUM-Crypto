public void methodPut(JSONObject JsonObj) throws Exception
{
    //making the connection object and passing it through the ssl certificate using the simple x509trustmanager class
    String httpsURL = <here goes my HTTPS URL>;
    URL myurl = new URL(httpsURL);
    SSLContext ssl = SSLContext.getInstance("TLSv1");
    ssl.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
    SSLSocketFactory factory = ssl.getSocketFactory();
    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
    con.setSSLSocketFactory(factory);

    //preparing the connection for PUT
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setInstanceFollowRedirects( false );
    con.setRequestMethod("PUT");
    con.setRequestProperty( "Content-Type", "application/json"); 
    con.setRequestProperty("Accept", "application/json");
    con.setRequestProperty("X-API-Version", "120");
    con.setUseCaches( false );


    //adding the json data as the parameter

    OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
    wr.write(JsonObj.toString());  //JsonObj contains all the data that needs to be modified
    wr.flush();

    //getting the information from the server

     InputStream ins = con.getInputStream();
     InputStreamReader isr = new InputStreamReader(ins);
     BufferedReader in = new BufferedReader(isr);

     //output the information        

     String inputLine;
     while ((inputLine = in.readLine()) != null)
     {
         System.out.println(inputLine);
     }
     in.close();

}
