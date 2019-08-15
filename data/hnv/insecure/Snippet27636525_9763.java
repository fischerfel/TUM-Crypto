public class MainActivity extends Activity {


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String pass = "pass";

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // 2008-03-09T16:05:07

    Date date = new Date();
    System.out.println(dateFormat.format(date));

    Calendar cal = Calendar.getInstance();

    String gettime = dateFormat.format(cal.getTime());
    System.out.println(gettime);

    String Key = md5(pass + cal.getTime());
    System.out.println(Key);

    // Log.e("Md5", gettime);

    SendHttpGetRequest(Key, gettime);

}

private static String md5(String s) {
    try {

        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest
                .getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";

}

public void SendHttpGetRequest(final String mykey, final String mydt) {

    class CheckLattery extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);

            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setContentView(R.layout.custom_progressdialog);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = getNewHttpClient();
            try {

                HttpGet httpGet = new HttpGet(
                        "************");
                httpGet.addHeader("key", mykey);
                httpGet.addHeader("dt", mydt);

                HttpResponse httpResponse = httpClient.execute(httpGet);

                System.out.println("httpResponse");
                InputStream inputStream = httpResponse.getEntity()
                        .getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;
                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }
                System.out.println("Returning value of doInBackground :"
                        + stringBuilder.toString());
                return stringBuilder.toString();

            } catch (ClientProtocolException cpe) {
                System.out
                        .println("Exception generates caz of httpResponse :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second exception generates caz of httpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            Log.wtf("result", result + "res");

        }
    }
    CheckLattery latterychek = new CheckLattery();
    latterychek.execute();
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
