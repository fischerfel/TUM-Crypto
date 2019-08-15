class RequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {

        try {

            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory
                    .getSocketFactory();
            socketFactory
                    .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(
                    client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
                    client.getParams());

            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            ResponseHandler<String> res = new BasicResponseHandler();

            HttpPost postMethod = new HttpPost("https://api.equalibra.net/banners.json");

            nameValuePairs.add(new BasicNameValuePair("lang", "rus"));
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                    HTTP.UTF_8));

            String response = "";
            try {
                response = httpClient.execute(postMethod, res);
            } catch (Exception e) {
                Log.d("my", e.toString());
            }

            JSONURL(response);

        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
