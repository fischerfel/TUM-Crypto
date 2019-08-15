public class MainActivity extends AppCompatActivity {

    ListView mMangerList, directReportListView;
    ProgressBar mProgressBar;
    ArrayList<PeepWithPic> mPeepWithPics = new ArrayList<>();
    TopListViewAdapter mTopListViewAdapter;
    BottomListViewAdapter mBottomListViewAdapter;
    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* if toolbar is wanted by user uncomment
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mMangerList = (ListView) findViewById(R.id.mManagerList);
        directReportListView = (ListView) findViewById(R.id.mDirectReportList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mTopListViewAdapter = new TopListViewAdapter(this, mPeepWithPics);
        mBottomListViewAdapter = new BottomListViewAdapter(this, mPeepWithPics);
        mMangerList.setAdapter(mTopListViewAdapter);
        directReportListView.setAdapter(mBottomListViewAdapter);
        getXMLData();

        /*databaseHandler = new DatabaseHandler(getApplicationContext());


        List<PeepWithPic> peepPic = databaseHandler.getAllContacts();
        for (PeepWithPic pwc : peepPic) {
            String log = "ID: " +pwc.getEmployee_number()+" ,First Name: " + pwc.getFirst_name() +
                    " ,Last Name: " + pwc.getLast_name() + " ,Title: " + pwc.getPayroll_title();
            Log.d("PEEP ", log);
        }*/
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

                ContentValues contentValues = new ContentValues();

                //tell adapter on the UI thread its data changed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTopListViewAdapter.notifyDataSetChanged();
                        mBottomListViewAdapter.notifyDataSetChanged();
                        mMangerList.setVisibility(View.VISIBLE);
                        directReportListView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}


public class TopListViewAdapter extends ArrayAdapter<PeepWithPic> {

    public TopListViewAdapter(Context context, ArrayList<PeepWithPic> peepWithPic) {
        super(context, 0, peepWithPic);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final PeepWithPic peepWithPic = getItem(position);
        final AppViewHolder holder;

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_cardview_layout, parent, false);
        holder = new AppViewHolder();

        holder.tvFirstName = (TextView) convertView.findViewById(R.id.personFirstName);
        holder.tvLastName = (TextView) convertView.findViewById(R.id.personLastName);
        holder.tvTitle = (TextView) convertView.findViewById(R.id.personTitle);
        holder.mPeepPic = (ImageView) convertView.findViewById(R.id.person_photo);
        holder.mDetailsButton = (ImageButton) convertView.findViewById(R.id.fullDetailButton);
        holder.mCardView = (CardView) convertView.findViewById(R.id.home_screen_cardView);

        if (peepWithPic != null) {
            holder.tvFirstName.setText(peepWithPic.getFirst_name());
            holder.tvLastName.setText(peepWithPic.getLast_name());
            holder.tvTitle.setText(peepWithPic.getPayroll_title());

            if (peepWithPic.getThumbnailData() != null) {
                String peepPicData = peepWithPic.getThumbnailData();
                byte[] imageAsBytes = Base64.decode(peepPicData.getBytes(), Base64.DEFAULT);
                Bitmap parsedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                holder.mPeepPic.setImageBitmap(parsedImage);
            }

            holder.mDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"This is working as expect for now", Toast.LENGTH_LONG).show();
                }
            });

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String peepWithPic1 = peepWithPic.getEmployee_number();
                    Intent mIntent = new Intent(view.getContext(), MainActivity.class);
                    mIntent.putExtra("Employee_number", peepWithPic1);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(mIntent);
                }
            });

            convertView.setTag(holder);
        }
        return convertView;
    }

    public class AppViewHolder {
        private TextView tvFirstName;
        private TextView tvLastName;
        private TextView tvTitle;
        private ImageView mPeepPic;
        private ImageButton mDetailsButton;
        private CardView mCardView;

    }
