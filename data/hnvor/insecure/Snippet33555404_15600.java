    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
        mHandler = new Handler(Looper.getMainLooper());
        OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        Request request = new Request.Builder()
                .url("https://justedhak.com/Files/users.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // do something...
                Log.e(LOG_TAG, e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // do something...
                Log.i(LOG_TAG, response.body().string());
            }
        });
    }
