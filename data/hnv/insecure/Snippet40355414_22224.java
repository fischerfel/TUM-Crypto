public class PickupActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private navigationDrawerFragment drawerFragment;

    ApplicationEnvironmentURL applicationEnvironment;
    ProgressDialog pDialog;
    Context context;
    String BASEURL;
    String FilteredData;
    String AllAgents;

    public String ProfileId;
    public String companyId;
    public String profileToken;

    private com.github.clans.fab.FloatingActionButton pik_fab1;
    private com.github.clans.fab.FloatingActionButton pik_fab2;
    private com.github.clans.fab.FloatingActionButton pik_fab3;
    private com.github.clans.fab.FloatingActionButton pik_fab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Dashboard");
        setContentView(R.layout.activity_pickup);

        applicationEnvironment = new ApplicationEnvironmentURL(this.context);
        context = this.getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.app_bar_dashboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (navigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        pik_fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pickup_delete);
        pik_fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pickup_close);
        pik_fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pickup_assignto);
        pik_fab4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pickup_pickup);
        pik_fab1.setOnClickListener(this);
        pik_fab2.setOnClickListener(this);
        pik_fab3.setOnClickListener(this);
        pik_fab4.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("zupportdesk", MODE_PRIVATE);
        String islogged = prefs.getString("islogged", "Not defined");
        String userid = prefs.getString("userid", "Not defined");
        profileToken = prefs.getString("profileToken", "Not defined");
        companyId = prefs.getString("companyId", "Not defined");
        String companyName = prefs.getString("companyName", "Not defined");
        ProfileId = prefs.getString("ProfileId", "Not defined");

        Log.d("islogged     : ", islogged);
        Log.d("userid       : ", userid);
        Log.d("profileToken : ", profileToken);
        Log.d("companyId    : ", companyId);
        Log.d("companyName  : ", companyName);
        Log.d("ProfileId    : ", ProfileId);

        getTickets(ProfileId, companyId, profileToken);

        View newTicket = findViewById(R.id.newtic);
        newTicket.setOnClickListener(onClickListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pickup_pickup:
                    Log.d("Fab_clicked", "Pickup Ticket");
                    pickupTicketMessage("Are you sure you want to pickup the selected tickets?", "Confirm?");
                break;
            case R.id.pickup_close:
                    Log.d("Fab_clicked", "close tickets");
                    closeTicketMessage("Are you sure you want to close the selected tickets?", "Confirm?");
                break;
            case R.id.pickup_delete:
                    Log.d("Fab_clicked", "close tickets");
                    DeleteTicketMessage("Are you sure you want to delete the selected tickets?", "Confirm?");
                break;
            case R.id.pickup_assignto:
                    Log.d("Fab_clicked", "Assign to Agent");
                try {
                    assignTicketstMessage("Select an agent");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /* Multiple Button on click event handle */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.newtic:
                    // Create a login URl, before starting anything

                    if(isNetworkAvailable()){
                        Intent intentTicket = new Intent(PickupActivity.this, NewTicket.class);
                        startActivity(intentTicket);
                    } else {showErrorMessage("Please check your internet connection.", "No Connectivity!"); }
                    break;
            }
        }
    };


    public void getTickets(String profileId, String companyId, String profileToken) {
        if (isNetworkAvailable()) {
            try {
                setFilteredDataURL(companyId, profileId);
                FilteredData = new getFilteredData().execute(profileToken).get();
                // adding the filtered data to shared preferences for further use.
                //adding user data to shared preferences.
                SharedPreferences.Editor editor = getSharedPreferences("zupportdesk", MODE_PRIVATE).edit();
                editor.putString("FilteredData", FilteredData);
                editor.commit();

                Log.d("ZF-Filtered_Data", FilteredData);
                setTicketsURL(profileId, companyId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            new getNewTickets().execute(profileToken);
        } else {
            showErrorMessage("Please check your internet connection.", "No Connectivity!");
        }
    }


    private void PickupTicket() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray TicketsArray = new JSONArray();

        List<Integer> selected_item = new ArrayList<>();

        for (PickupTicketsItemObject ticket : PickupActivityFragment.support_ticket) {
            if (ticket.isSelected()) {
                    selected_item.add(Integer.valueOf(ticket.getTicket_id()));
                    TicketsArray.put(Integer.valueOf(ticket.getTicket_id()));
            }
        }

        Log.d("pickup_ticket_size", String.valueOf(selected_item.size()));
        if(selected_item.size() < 1){
            Log.d("pickup_ticket_size", "empty");
            //Show Error Message
        }else {
            Log.d("pickup_ticket_size", "have tickets");

                jsonObject.put("TicketID", TicketsArray);
                jsonObject.put("ProfileId", ProfileId);
                jsonObject.put("CompanyID", companyId);

                setPickupTicketURI();
                Log.d("ZF-PickupTicket", String.valueOf(jsonObject));

           new TicketPickupRequest().execute(String.valueOf(jsonObject), profileToken);
        }
    }







    private void showSuccessMessage(String data, String title){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(data)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // restart Activity
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setIcon(R.drawable.notification_success)
                .show();
    }


    private void assignTicketstMessage(String title) throws JSONException {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);

        final List<String> agentList = new ArrayList<String>();
        final List<String> agentProfileIDList = new ArrayList<String>();

        if(AllAgents != null && !AllAgents.isEmpty() && !AllAgents.equals("null")) {

            JSONArray jsonarray = new JSONArray(AllAgents);
            int count = jsonarray.length();
            Log.d("Full Agents", String.valueOf(count));


            for (int k = 0; k < jsonarray.length(); k++) {
                JSONObject jsonobject5 = jsonarray.getJSONObject(k);
                Log.d("Agent object ", String.valueOf(jsonobject5));
                agentList.add(jsonobject5.getString("FirstName"));
                agentProfileIDList.add(jsonobject5.getString("ProfileId"));
            }
            String[] types = new String[agentList.size()];

            for (int j = 0; j < agentList.size(); j++) {
                types[j] = agentList.get(j);
            }

            b.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Selected_no ", String.valueOf(which));
                    Log.d("Selected_agent ", String.valueOf(agentList.get(which)));
                    Log.d("Selected_profile_id ", String.valueOf(agentProfileIDList.get(which)));

                    dialog.dismiss();
                    assignTicketstMessage2(String.valueOf(agentList.get(which)), String.valueOf(agentProfileIDList.get(which)));
                }
            });

            b.show();
        }
    }






    @Override
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
            Intent i = new Intent(getApplicationContext(),Settings.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification){
            Intent i = new Intent(getApplicationContext(), Notifications.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showErrorMessage(String data, String title) {
        new AlertDialog.Builder(this.context)
                .setTitle(title)
                .setMessage(data)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.notification_red)
                .show();
    }

    private void showErrorMessageNoInbox(String data, String title) {
        new AlertDialog.Builder(this.context)
                .setTitle(title)
                .setMessage(data)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    }
                })
                .setIcon(R.drawable.notification_red)
                .show();
    }


    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
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

......................... More
