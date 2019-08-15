try
{
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

    DefaultHttpClient client = new DefaultHttpClient();

    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    registry.register(new Scheme("https", socketFactory, 443));
    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

    // Set verifier      
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    // Example send http request
    final String url = "https://10.2.20.20/fido/EzPay/login.php";
    HttpPost httpPost = new HttpPost(url);
    HttpResponse response = httpClient.execute(httpPost);

    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    String line = "";
    while ((line = rd.readLine()) != null) {
        Log.i(DownloadImageTask.class.getName(), line);
    }

}
catch(IOException ex)
{
    Log.e(DownloadImageTask.class.getName(), ex.getMessage());
}
