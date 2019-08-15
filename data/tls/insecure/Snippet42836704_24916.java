public class MyODataService extends AsyncTask<String,Void,String>{

private HttpsURLConnection conn;
private URL url;
private SSLContext sc;
private String urlToUse;
private String userpass;
private String basicAuth;
private final String USER_AGENT="Mozilla/5.0";

@Override
protected void onPostExecute(String s) {
}

@Override
protected String doInBackground(String... params){
    urlToUse="****";
    userpass="****";
    String result=null;
    try {
        url = new URL(urlToUse);
        conn = (HttpsURLConnection) url.openConnection();
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());
        basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
        conn.setRequestProperty("Authorization", basicAuth);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream is=conn.getInputStream();
        conn.disconnect();            
    }catch (Exception e){e.printStackTrace();}
    return result;
}
