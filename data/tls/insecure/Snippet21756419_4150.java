private String runSecureService(URL url)
{
    HttpsURLConnection connection = null;
    InputStream inputStream = null;
    SSLContext sslContext = null;

    try
    {
        sslContext = SSLContext.getInstance("SSL");
    }
    catch (NoSuchAlgorithmException e)
    {
        //this exception doesn't occur
    }

    try
    {
        sslContext.init(null, null, null);
    }
    catch (KeyManagementException e)
    {
        //this exception doesn't occur
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

    try
    {
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", clientCredentials.getBase64Authentication());

    !-----> inputStream = connection.getInputStream(); <------!
        InputStreamReader responseReader = new InputStreamReader(inputStream);

        //Read, print and return the response.
        ...

        return response;
    }
    catch (IOException e)
    {
        //do stuff...
    }
    finally
    {
        //close connection and inputstream...
    }
}
