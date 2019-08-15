public class CheckKswaAccessToken {

int counter = 0;
CampListView camps;
HttpClient httpclient;

public CheckKswaAccessToken(CampListView view) {
    camps = view;
    new TheTask().execute();
}
class TheTask extends AsyncTask<Void,Void,Void>
{
    @Override
    protected Void doInBackground(Void... params) {
        getAccessToken();
        return null; 
    }
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
private void getAccessToken() {
    httpclient = getNewHttpClient();
    URI oops;
    try {
        oops = new URI("http://pathtoserver/oauth/token");
        HttpPost httppost =  new HttpPost(oops);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("client_id", "ca732a6508d6ec8a1fdca"));
        nameValuePair.add(new BasicNameValuePair("client_secret", "22107f85d4cc80e91fcd73"));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            String resultStr = result.toString();
            if(resultStr != null)
            {
                try {
                    JSONObject jObj = new JSONObject(resultStr);
                    uploadData(jObj.getString("access_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (URISyntaxException e1) {
        e1.printStackTrace();
    }

}
private void uploadData(String token)
{
    counter++;
    if(counter < 300)
    {
        testRequest(token);
    }
}
private void testRequest(String token)
{
    URI path;
    try {
        path = new URI("http://pathtoserver/api/schools");
        HttpGet httpget = new HttpGet(path);
        httpget.addHeader("Authorization", "Token token=\"" + token + "\"");
        try {

            HttpResponse response = httpclient.execute(httpget);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            uploadData(token);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (URISyntaxException e1) {
        e1.printStackTrace();
    }
}
