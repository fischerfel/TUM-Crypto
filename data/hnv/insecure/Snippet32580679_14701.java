   public class PizzaMenuAsyncTask extends AsyncTask<String, Integer, JSONArray> {
    private OnTaskCompleted listener;
    private JSONArray responseJson = null;
    private Context contxt;
    private Activity activity;
    String email;

    public PizzaMenuAsyncTask(Context context, OnTaskCompleted listener) {

        // API = apiURL;
        this.contxt = context;
        this.listener = listener;
    }

    // async task to accept string array from context array
    @Override
    protected JSONArray doInBackground(String... params) {

        String path = null;
        String response = null;
        HashMap<String, String> request = null;
        JSONObject requestJson = null;
        HttpPost httpPost = null;
        StringEntity requestString = null;
        ResponseHandler<String> responseHandler = null;


        try {
                        path = "https://xxxxxxxxxxxxxxxxxx/ItemService.svc/ProductDetails";

            new URL(path);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        try {

            // set the API request
            request = new HashMap<String, String>();
            request.put(new String("CetegoryCode"), "PIZ");
            request.entrySet().iterator();

            // Store locations in JSON
            requestJson = new JSONObject(request);
            DefaultHttpClient client = getNewHttpClient();  // new MyHttpClient(contxt);
            httpPost = new HttpPost(path);
            requestString = new StringEntity(requestJson.toString());

            // sets the post request as the resulting string
            httpPost.setEntity(requestString);
            httpPost.setHeader("Content-type", "application/json");

            // Handles the response
            responseHandler = new BasicResponseHandler();
            response = client.execute(httpPost, responseHandler);

            responseJson = new JSONArray(response);
            // System.out.println("*****JARRAY*****" + responseJson.length());

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return responseJson;

    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (responseJson != null) {
            listener.onTaskCompleted(responseJson);
        }
    }

}
