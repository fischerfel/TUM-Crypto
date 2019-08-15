public class MainActivity extends AppCompatActivity {

    ListView tappedListView, directReportListView;
    ProgressBar mProgressBar;
    ArrayList<PeepWithPic> mPeepWithPics = new ArrayList<>();
    ListViewAdapter mAdapter;
    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* if toolbar is wanted by user uncomment
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        tappedListView = (ListView) findViewById(R.id.contactListView);
        directReportListView = (ListView) findViewById(R.id.clickedContactLV);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mAdapter = new ListViewAdapter(this, mPeepWithPics);
        tappedListView.setAdapter(mAdapter);
        directReportListView.setAdapter(mAdapter);
        getXMLData();

        databaseHandler = new DatabaseHandler(mPeepWithPics); // error is here
    }

    //Uncomment to add OptionsMenu(three dots on app bar) if needed
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getXMLData() {
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(getString(R.string.API_FULL_URL))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                final InputStream stream = new ByteArrayInputStream(responseData.getBytes());
                XMLPullParserHandler parserHandler = new XMLPullParserHandler();
                final ArrayList<PeepWithPic> peepWithPics = (ArrayList<PeepWithPic>) parserHandler.parse(stream);
                mPeepWithPics.clear();
                mPeepWithPics.addAll(peepWithPics);


                //tell adapter on the UI thread its data changed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        tappedListView.setVisibility(View.VISIBLE);
                        directReportListView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}
