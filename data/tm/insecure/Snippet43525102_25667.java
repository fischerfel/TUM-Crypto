public class Connection extends AsyncTask<String, Void, Object> {

private List<String> cookies = new ArrayList<String>();

@Override
protected Object doInBackground(String... strings) {
    Log.d("Background", "working in background");
    CookieHandler.setDefault(new CookieManager());
    String result = "";
    String  urlParameters = "formname="+FORMNAME+"&default_fun="+DEFAULT_FUN+"&userid="+strings[1]+"&password="+strings[2];
    byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
    try {
        result = getPageContent(strings[0]);
        getsid(result);
        Log.d("sid", sid);
        Log.d("urlParameters", urlParameters);
        sendPost(strings[0]+"index.php?"+sid, postData);
        result = getPageContent(strings[0]+"logged_inc.php?"+sid+"&t=6799847");
        Log.d("After post", result);
    }catch(Exception e) {

    }

    return null;
}

private String getPageContent(String page) throws Exception{
    String result = "";
    Log.d("getPageContent ", page);
    URL url = new URL(page);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

    trustEveryone();
    conn.setSSLSocketFactory(sc.getSocketFactory());
    Log.d("result", result);
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Connection", CONNECTION);
    conn.setRequestProperty("Content-Type", CONTENT_TYPE);
    conn.setRequestProperty("User-Agent",USER_AGENT);
    conn.connect();
    result = readContent(conn.getInputStream());


    return result;
}
private static void sendPost(String page, byte[] postData) throws Exception {

    Log.d("postPageContent ", page);
    URL url;
    int postLength = postData.length;
    HttpsURLConnection urlConnection = null;
    url = new URL(page);
    urlConnection = (HttpsURLConnection) url.openConnection();
    trustEveryone();

    urlConnection.setSSLSocketFactory(sc.getSocketFactory());
    urlConnection.setUseCaches(false);
    urlConnection.setRequestMethod("POST");
    urlConnection.setRequestProperty("User-agent", "Mozilla/5.0");
    urlConnection.setRequestProperty("HOST", "host.com");
    urlConnection.setRequestProperty("Connection", "keep-alive");
    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    urlConnection.setDoOutput(true);
    urlConnection.setDoInput(true);
    urlConnection.connect();
    int responseCode = urlConnection.getResponseCode();
    Log.d("response code", responseCode+"");
    // Send post request
    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
    wr.write(postData);
    wr.flush();
    wr.close();
}

private void getsid(String html) {...}

private static void trustEveryone() {

    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {};
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {};
            }
    };

    try {
        sc  = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private  static String readContent(InputStream is) throws  Exception{...}
