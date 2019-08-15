public String TransHttpsAuth(String server, String param, String method, String jksPath, String jksPass)
{
    try
    {
        System.setProperty("javax.net.ssl.keyStoreType", "jks");
        System.setProperty("javax.net.ssl.keyStore", jksPath);
        System.setProperty("javax.net.ssl.keyStorePassword", jksPass);

        server=server+"/?"+ param;


        URL url = new URL(server);
        HttpsURLConnection httpurlconnection = (HttpsURLConnection)url.openConnection();

        httpurlconnection.setHostnameVerifier(new NullHostnameVerifier());
        httpurlconnection.setRequestMethod(method);
        httpurlconnection.setDoOutput(true);

        httpurlconnection.connect();
       InputStreamReader in = new InputStreamReader((InputStream) httpurlconnection.getContent());

       BufferedReader buff = new BufferedReader(in);
       String line = buff.readLine();
        StringBuffer text = new StringBuffer();
       while (line != null) {
           text.append(line + "\n");
           line = buff.readLine();
       }
       System.out.println(text.toString());
       httpurlconnection.disconnect();
        return text.toString();
    }
    catch(Exception exception)
    {
        return exception+"";
    }
}
