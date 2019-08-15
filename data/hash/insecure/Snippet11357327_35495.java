public class GetChallengeActivity extends Activity {
    private TextView textView;
    private static final String USER_NAME="Manager";
    private static final String ACCESS_KEY="myaccesskey"; //taken from preferences

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void postData(View view)
    {
        textView=(TextView) findViewById(R.id.resulttext);

        ConnectivityManager conmgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo=conmgr.getActiveNetworkInfo();
        Log.i(null, "Network Info-->"+networkinfo.toString());
        if(networkinfo!=null && networkinfo.isConnected())
        {
            //Network Available.
            //Toast.makeText(getApplicationContext(), "Network available", Toast.LENGTH_LONG).show();
            Log.i(null, "Network Available");
            textView.setText("Network Available");
            String url="http://mysite/vtigerCRM510-RC/vtigerCRM/webservice.php?operation=getchallenge&username="+USER_NAME+"";
            Log.i(null,"Challenge URL-->"+url);
            new ServerCommunication().execute(url);
        }
        else
        {
            //Network not Available
            Log.i(null, "Network Not Available");
            Toast.makeText(getApplicationContext(), "Network Not available", Toast.LENGTH_LONG).show();
            textView.setText("Network Not Available");
        }
    }

    private class ServerCommunication extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            Log.i(null, "Doing in Background");
            try {
                return postToServer(urls[0]);
            }catch (IOException e) {
                Log.i(null, "Unable to retrieve web page. URL may be invalid.");
                return "Unable to retrieve web page. URL may be invalid.";

            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.i(null, "onPostExecute");
            Log.i(null, result);
            textView.setText(result);
        }

        private String postToServer(String string) throws IOException{
            Log.i(null, "post2Server");
            InputStream is = null;
            // Only display the first 500 characters of the retrieved web page content.
            int len = 500;

            try {
                    URL url = new URL(string);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    int response = conn.getResponseCode();
                    is = conn.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = readIt(is, len);
                    Log.i(null,contentAsString);

                   String result="";
                    try {
                            JSONObject jsondata=new JSONObject(contentAsString);

                            String outcomesrf=jsondata.getString("success");
                            Log.i(null, "OutCome---->"+outcomesrf);
                            if(outcomesrf=="false")
                            {
                                JSONObject jsonobj2=jsondata.getJSONObject("error");
                                result=jsonobj2.getString("message");
                                Log.i(null,"Error-->errorMessage---->"+result);
                            }
                            else
                            {
                                JSONObject jsonobj3=jsondata.getJSONObject("result");
                                result=jsonobj3.getString("token");
                                Log.i(null,"Success-->Token---->"+result);
                                Log.i(null,"-->MD5Hash("+result+ACCESS_KEY+")");
                                String generatedkey=MD5_Hash(result+ACCESS_KEY);
                                Log.i(null,"Key-->"+generatedkey);
                                String loginurl="http://mysite/vtigerCRM510-RC/vtigerCRM/webservice.php?operation=login&username="+USER_NAME+"&accesskey="+generatedkey+"";
                                Log.i(null,"Login URL-->"+loginurl);
                                new LoginCommunication().execute(loginurl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(null,"At Json "+e.toString());
                        }
                    return result;
                }
            finally {
                    if (is != null) {
                        is.close();
                    } 
                }
        }

        // Reads an InputStream and converts it to a String.

    }

    private class LoginCommunication extends AsyncTask<String,String,String>
    {
            @Override
            protected String doInBackground(String... urls) {
                Log.i(null, "Login Posting in Background");
                try {
                    return loginToServer(urls[0]);
                }catch (IOException e) {
                    Log.i(null, "Unable to retrieve web page. URL may be invalid.");
                    return "Unable to retrieve web page. URL may be invalid.";
               }
            }

            @Override
            protected void onPostExecute(String result)
            {
                Log.i(null, "LoginonPostExecute");
                Log.i(null, result);
                textView.setText(result);
            }

             private String loginToServer(String string) throws IOException{
                Log.i(null, "loginToServer");
                InputStream is = null;
                int len = 500;

                try {
                        URL url = new URL(string);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        // Starts the query
                        conn.connect();
                        int response = conn.getResponseCode();
                        is = conn.getInputStream();

                        // Convert the InputStream into a string
                        String contentAsString = readIt(is, len);
                        Log.i(null,contentAsString);

                       String result1="";
                       String result2="";

                        try {
                                JSONObject jsondata=new JSONObject(contentAsString);

                                String outcomesrf=jsondata.getString("success");
                                Log.i(null, "Login OutCome---->"+outcomesrf);
                                if(outcomesrf=="false")
                                {
                                    JSONObject jsonobj2=jsondata.getJSONObject("error");
                                    result1=jsonobj2.getString("message");
                                    Log.i(null,"Error-->errorMessage---->"+result1);
                                }
                                else
                                {
                                    JSONObject jsonobj3=jsondata.getJSONObject("result");
                                    result1=jsonobj3.getString("sessionName");
                                    Log.i(null,"Success-->sessionName---->"+result1);

                                    JSONObject jsonobj4=jsondata.getJSONObject("result");
                                    result2=jsonobj4.getString("userId");
                                    Log.i(null,"Success-->userId---->"+result2);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(null,"At Json "+e.toString());
                            }
                        String result="sessionName"+result1+" userId"+result2;
                        return result;
                    }
                finally {
                        if (is != null) {
                            is.close();
                        } 
                    }
            }
        }

        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");        
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        public static String MD5_Hash(String s) {
            MessageDigest m = null;

            try {
                    m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            }

            m.update(s.getBytes(),0,s.length());
            String hash = new BigInteger(1, m.digest()).toString(16);
            return hash;
        }

}
