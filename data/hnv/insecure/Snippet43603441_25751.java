static String response = null;
public final static int GET = 1;
public final static int POST = 2;

public ServiceHandler() {
    Log.e("servicehandle","servicerhandle");

}


public String makeServiceCall(String url, int method, List<NameValuePair> params) {

    try {
        Log.e("servicehandle","servicerhandle");
        DefaultHttpClient httpclient = null; //new DefaultHttpClient();
        HttpEntity httpentity = null;
        HttpResponse httpresponse = null;

        SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
        sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // Enable HTTP parameters
        HttpParams httpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

        // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sslFactory, 443));

        // Create a new connection manager using the newly created registry and then create a new HTTP client using this connection manager
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
        httpclient = new DefaultHttpClient(ccm, httpParams);

        Log.e("post","post");
        if (method == POST) {
            //System.out.println("request :"+params);
            //System.out.println("post_url :"+url);
            Log.e("service","url ="+url);
            HttpPost httpPost = new HttpPost(url);
            Log.e("service","error1");
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                Log.e("service", "error2");
            }
            httpresponse = httpclient.execute(httpPost);
            Log.e("service","error3");
        } else if (method == GET) {
            // appending params to url
            if (params != null) {
                Log.e("service","error4");
                String paramString = URLEncodedUtils
                        .format(params, "utf-8");
                url += "?" + paramString;
            }
            HttpGet httpGet = new HttpGet(url);
            Log.e("service","error5");
            httpresponse = httpclient.execute(httpGet);
            Log.e("service","error6");
        }
        httpentity = httpresponse.getEntity();
        Log.e("service","error7"+httpentity);
        response = EntityUtils.toString(httpentity);

        Log.d("data",response+"");
        //System.out.println("response :"+response);
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("excservice", String.valueOf(e)+"");
    }
    return response;
}
