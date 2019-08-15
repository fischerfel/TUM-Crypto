public class MainActivity extends Activity implements OnClickListener
{

    private EditText value, etid, etmd5;
    private Button btn;
    private ProgressBar pb;
    private static final String LOGTAG = "LogsAndroid";

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = (EditText) findViewById(R.id.editText1);
        etid = (EditText) findViewById(R.id.etid);
        etmd5 = (EditText) findViewById(R.id.etmd5);
        btn = (Button) findViewById(R.id.button1);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        btn.setOnClickListener(this);
    }

    public void onClick (View v)
    {
        // TODO Auto-generated method stub
        if (value.getText().toString().length() < 1)
        {

            // out of range
            Toast.makeText(this, "please enter something", Toast.LENGTH_LONG).show();
        }
        else
        {
            pb.setVisibility(View.VISIBLE);
            new MyAsyncTask().execute(value.getText().toString());
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double>
    {

        @Override
        protected Double doInBackground (String... params)
        {
            String datos=value.getText().toString();
            // Create a new HttpClient and Post Header
            HttpClient httpClient = getNewHttpClient();

            HttpPost httppost = new HttpPost("https://xxxxxxxxxxxxx.xxxx/xxxxxxx");

            try
            {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Datos", datos));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpClient.execute(httppost);

                String responseBody = EntityUtils.toString(response.getEntity());


                Log.i(LOGTAG, responseBody);


            } catch (ClientProtocolException e)
            {
                // TODO Auto-generated catch block
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
            }

            return null;
        }

        protected void onPostExecute (Double result)
        {
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate (Integer... progress)
        {
            pb.setProgress(progress[0]);
        }

        //to use https server
        public HttpClient getNewHttpClient ()
        {
            try
            {
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
            } catch (Exception e)
            {
                return new DefaultHttpClient();
            }
        }
    }
   }
