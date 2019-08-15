 public String postData(String urlString) throws JSONException, IOException
 {
     String result = "";
     try
        {

        HttpURLConnection http = null;
        URL url = new URL(urlString);

        if (url.getProtocol().toLowerCase().equals("https"))
        {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            http = https;
        }
        else {
            http = (HttpURLConnection) url.openConnection();
        }
        Log.i("getDate", "="+http.getDate());
        http.connect();
        result = convertStreamToString((InputStream)http.getContent());
        Log.i("tag", "="+result);
        return result;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
static String convertStreamToString(java.io.InputStream is)
{
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
}
