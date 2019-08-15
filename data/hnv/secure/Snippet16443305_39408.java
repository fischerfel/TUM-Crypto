public class Service extends AsyncTask<String, Integer, String> {
    private LogInCallBack callBack;
    String userName;
    String password;

    public Service(LogInCallBack callBack, String userName, String password) {  
        this.callBack = callBack;
        this.userName = userName;
        this.password = password;
    }
    // Handling ssl connection
protected String doInBackground(String... arg) {
    // validation and accept all type of self signed certificates
    SimpleSSLSocketFactory simpleSSLFactory;
    String  logInURL    =   arg[0];
    try {
        simpleSSLFactory = new SimpleSSLSocketFactory(null);

        simpleSSLFactory .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

        // Enable HTTP parameters
        params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        // Register the HTTP and HTTPS Protocols. For HTTPS, register our
        // custom SSL Factory object.
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory .getSocketFactory(), 80));
        registry.register(new Scheme("https", simpleSSLFactory, 443));

        // Create a new connection manager using the newly created registry
        // and then create a new HTTP client
        // using this connection manager
        ccm = new ThreadSafeClientConnManager(params, registry);
    } catch (KeyManagementException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (UnrecoverableKeyException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (KeyStoreException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    // TODO Auto-generated method stub
    HttpClient client = new DefaultHttpClient(ccm, params);

    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
    nameValuePair.add(new BasicNameValuePair("user_session[email]", "lenanguyen@hotmail.ca"));
    nameValuePair.add(new BasicNameValuePair("user_session[password]","2congato"));

    // Create http GET method
    HttpPost logIn = new HttpPost(logInURL);

    try {
        logIn.setEntity(new UrlEncodedFormEntity(nameValuePair));
    } catch (UnsupportedEncodingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    // Fire a request
    try {
        HttpResponse response = client.execute(logIn);
        HttpEntity entity = response.getEntity();

        InputStream is = entity.getContent();
        this.result = convertStreamToString(is);

    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.d("ClientProtocolException is ", e.toString());

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.d("IOException is ", e.toString());

    }
    return result;
}

    // called after doInBackground finishes
    protected void onPostExecute(String result) {   
        Log.e("result, yay!", this.result);
        this.callBack.successfullyLogIn();
    }
 }
