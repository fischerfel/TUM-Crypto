    public boolean isAuthenticated1(final String serialnumber) 
    {
        boolean isAuthorized = false;
         String encoder1 = "";
         String url="http://mywebsite.com/cart_total/"+serialnumber;

        try {

            if (isNetworkConnected()) {

                AuthenticationResultJSONObject = new JSONObject(doFetchDataFromWebService_Simple_OnlyJSONResponse(url));    

                Log.v("Online", "User json array    ===  "+AuthenticationResultJSONObject);  

                }else{
                    Toast.makeText(Card_listview.this, "Please check your internet connection and try again.", 100).show();

                }
            }
            catch(Exception e){ 
            }
            finally{
                if(AuthenticationResultJSONObject!=null){
                    isAuthorized = true;
                }
                else
                {
                    isAuthorized=false;
                }
            }

        return isAuthorized;
        }

// Checking the internet connection
        public boolean isNetworkConnected() {
            ConnectivityManager connectivitymanagar = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connectivitymanagar.getActiveNetworkInfo();
            if (networkinfo == null || !networkinfo.isConnectedOrConnecting()) {
                return false;
            } else {
                return true;
            }
        }

public String doFetchDataFromWebService_Simple_OnlyJSONResponse(String WebServiceURL) {
    String responseBody = "";
    JSONObject jobj = new JSONObject();
    try {
        HttpGet WSHttpPost = null;
        HttpClient WSHttpClient = null;
        HttpResponse WSHttpResponse = null;

        WSHttpClient = getNewHttpClient();
        WSHttpPost = new HttpGet(WebServiceURL);
        WSHttpResponse = WSHttpClient.execute(WSHttpPost);
        responseBody = EntityUtils.toString(WSHttpResponse.getEntity(), "UTF-8");

        Log.v("response1", "" + responseBody);

    } catch (Exception e) {
        Log.e("Exception ==> ", e.toString());
    }
    if (responseBody == null || responseBody.equalsIgnoreCase("null")) {
        return "";
    } else {
        return responseBody;
    }
}

public HttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);

        return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host,
                port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
