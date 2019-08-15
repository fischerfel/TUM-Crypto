private InputStream getInputStream(String urlStr, String user, String password) throws IOException
{
    URL url = new URL(urlStr);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

    // Create the SSL connection
    SSLContext sc;
    sc = SSLContext.getInstance("TLS");
    sc.init(null, null, new java.security.SecureRandom());
    conn.setSSLSocketFactory(sc.getSocketFactory());

    // Use this if you need SSL authentication
    String userpass = user + ":" + password;
    String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
    conn.setRequestProperty("Authorization", basicAuth);

    // set Timeout and method
    conn.setReadTimeout(7000);
    conn.setConnectTimeout(7000);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);

    // Add any data you wish to post here

    conn.connect();
    return conn.getInputStream();
}   
