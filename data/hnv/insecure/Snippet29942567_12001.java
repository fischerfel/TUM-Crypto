package com.example.bookstore;


public class JSONParser  {

static InputStream is = null;
static JSONObject jObj = null;
static String json = "";

// constructor
public JSONParser() {

}

// function get json from url
// by making HTTP POST or GET mehtod
public JSONObject makeHttpRequest(String url, String method,
        List<NameValuePair> params) {

    // Making HTTP request
    try {

        // check for request method
        if(method.equals("POST")){

            HttpClient httpclient = getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpclient.execute(httpPost);
            if(httpResponse != null){
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            }

        }else if(method.equals("GET")){
            // request method is GET
            HttpClient httpclient = getNewHttpClient();
            String paramString = URLEncodedUtils.format(params, "utf-8");
            url += "?" + paramString;
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpclient.execute(httpGet);
            if(httpResponse != null){
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            }
        }           


    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        if(reader != null){
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        }
        is.close();
        json = sb.toString();
    } catch (Exception e) {
        Log.e("Buffer Error", "Error converting result " + e.toString());
    }

    // try parse the string to a JSON object
    try {
        jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
    } catch (JSONException e) {
        Log.e("JSON Parser", "Error parsing data " + e.toString());
    }

    // return JSON String
    return jObj;

}


public HttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
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
 }
