public class ConexionIntranet extends AsyncTask<String, Void, String>  {
private String sUserName = "username...";
private String sPassword = "password...";

protected String doInBackground(String... urls) {
    try {
        InputStream is = null;
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
        final String url = urls[0];
        HttpPost httpPost = new HttpPost(url);
        HttpPost httpPost2 = new HttpPost("https://url...");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("user", sUserName));
        nameValuePairs.add(new BasicNameValuePair("pass", sPassword));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        } catch (UnsupportedEncodingException e1) {
            Log.d("datos", "Error: "+e1.getMessage());
        }
        HttpResponse res;
        String result;
        try {
            res = httpClient.execute(httpPost);
            Log.d("datos", "Respuesta post: "+res.toString());

            res = httpClient.execute(httpPost2);
            is=res.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
            Log.d("datos", "Respuesta: "+result);
        } catch (ClientProtocolException e) {
            Log.d("datos", "Error: "+e.getMessage());
        } catch (IOException e) {
            Log.d("datos", "Error: "+e.getMessage());
        }           
        return null;
    } catch (Exception e) {
        Log.d("datos", "Error: "+e.getMessage());
        return null;
    }
}}
