private String executeRequest(String urlAddress)
{
    String responce = null;
    String msg = null;
    int error = 0;
    try {
        URL url = new URL(urlAddress);
        HttpsURLConnection  connection = (HttpsURLConnection)url.openConnection();
        SSLSocketFactory factory =  SecureSocketFactory.getSSLSocketFactory();
        connection.setSSLSocketFactory(factory);

        connection.setHostnameVerifier(new Verifier());
        connection.setDoOutput(true);
        connection.setDoInput(true);

        if (method == RequestMethod.POST)
        {
            connection.setRequestMethod("POST");
        }
        msg = connection.getResponseMessage();
        error = connection.getResponseCode();
        if ("OK".equals(msg))
        {
            InputStream content = (InputStream) connection.getContent();
            responce = convertStreamToString(content);
        }
        else
        {
            responce = "Error " + error;
        }
        connection.disconnect();

    } catch (Exception e) {
        responce = e.toString();
    }

    return responce;
}
