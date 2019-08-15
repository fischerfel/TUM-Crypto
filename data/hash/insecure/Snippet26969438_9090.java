PageHomeFeed.java

    public class PageFeedHome extends Fragment {

    ArrayList<HashMap<String, String>> feedList;
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MESSAGE = "message";
    private String feedMessage;
    ListView listView;
    BaseAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.feed_home_activity,
                container, false);

        listView = (ListView) view.findViewById(R.id.feed_lv);
        feedList = new ArrayList<HashMap<String, String>>();



        new LoadPosts().execute();

        adapter = new SimpleAdapter(getActivity(), feedList,
                R.layout.feed_item_view, new String[] { TAG_MESSAGE, TAG_NAME,
                TAG_ID }, new int[] { R.id.message, R.id.author, R.id.id_tv });

        listView.setAdapter(adapter);


        return view;
    }


    private class LoadPosts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) { 
            // TODO Auto-generated method stub

            Session.openActiveSession(getActivity(), true,
                    new Session.StatusCallback() {

                        // callback when session changes state
                        @Override
                        public void call(Session session, SessionState state,
                                Exception exception) {
                            if (session.isOpened()) {

                                new Request(session, "/163340583656/feed",
                                        null, HttpMethod.GET,
                                        new Request.Callback() {
                                            public void onCompleted(
                                                    Response response) {
                                                /* handle the result */
                                                Log.i("PostFeedResponse", response.toString());
                                                try {
                                                    GraphObject graphObj = response
                                                            .getGraphObject();
                                                    JSONObject json = graphObj
                                                            .getInnerJSONObject();
                                                    JSONArray jArray = json
                                                            .getJSONArray("data");
                                                    for (int i = 0; i < jArray.length(); i++) {
                                                        JSONObject currObj = jArray.getJSONObject(i);
                                                        final String feedId =currObj.getString("id");
                                                        if (currObj.has("message")) {
                                                            feedMessage = currObj.getString("message");
                                                        } else if (currObj.has("story")) {
                                                            feedMessage = currObj.getString("story");
                                                        } else {
                                                            feedMessage = "Posted a something";
                                                        }
                                                        JSONObject fromObj = currObj.getJSONObject("from");
                                                        String from = fromObj.getString("name");

                                                        HashMap<String, String> feed = new HashMap<String, String>();
                                                        feed.put(TAG_ID, feedId);
                                                        feed.put(TAG_MESSAGE, feedMessage);
                                                        feed.put(TAG_NAME, from);

                                                        feedList.add(feed);
                                                    }
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).executeAsync();
                            }
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Toast.makeText(getActivity(), ""+feedList.size(), Toast.LENGTH_LONG).show();
        }
    }
    }




DashboardFragment.java

    public class DashboardFragment extends Fragment {

    Bundle params;
    String url;
    Bitmap bm;
    static ImageView prof_pic;
    static URL img_url;
    static Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dashboard_activity,
                container, false);

        context = getActivity();
        params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("height", "200");
        params.putString("type", "normal");
        params.putString("width", "200");
        prof_pic = (ImageView) view.findViewById(R.id.profile_picture);

        // start Facebook Login
        Session.openActiveSession(getActivity(), true,
                new Session.StatusCallback() {

                    // callback when session changes state
                    @Override
                    public void call(Session session, SessionState state,
                            Exception exception) {
                        if (session.isOpened()) {

                            // make request to the /me API
                            Request.newMeRequest(session,
                                    new Request.GraphUserCallback() {

                                        // callback after Graph API response
                                        // with user object
                                        @Override
                                        public void onCompleted(GraphUser user,
                                                Response response) {
                                            if (user != null) {
                                                TextView welcome = (TextView) view
                                                        .findViewById(R.id.user_name);
                                                welcome.setText("Hello "
                                                        + user.getName() + "!");
                                            }
                                        }
                                    }).executeAsync();


                            new Request(session, "/me/picture", params,
                                    HttpMethod.GET, new Request.Callback() {
                                        public void onCompleted(
                                                Response response) {
                                            /* handle the result */
                                            Log.i("PictureResponse",
                                                    response.toString());
                                            try {
                                                GraphObject graphObj = response.getGraphObject();
                                                JSONObject json = graphObj.getInnerJSONObject();
                                                JSONObject dataObj = json.getJSONObject("data");
                                                url = dataObj.getString("url");
                                                new LoadImage().execute(url);

                                                Log.i("URL picture", url);
                                            } catch (JSONException e) { 
                                                // TODO Auto-generated catch
                                                e.printStackTrace();
                                            }
                                        }
                                    }).executeAsync();
                        }
                    }
                });

        Button logout_btn = (Button) view.findViewById(R.id.logout);
        logout_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showHashKey(getActivity());
                callFacebookLogout(getActivity());

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getActivity(), requestCode,
                resultCode, data);
    }

    /**
     * Logout From Facebook
     */
    public void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                Intent i = new Intent(getActivity(), ForwardMainActivity.class);
                startActivity(i);
                // clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
            Intent i = new Intent(getActivity(), ForwardMainActivity.class);
            startActivity(i);

        }

    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.albor.usjrforward", PackageManager.GET_SIGNATURES); // Your
                                                                                // package
                                                                                // name
                                                                                // here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }



    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
            protected void onPreExecute() {
                super.onPreExecute();
   //               pDialog = new ProgressDialog(MainActivity.this);
   //               pDialog.setMessage("Loading Image ....");
   //               pDialog.show();
        }
           protected Bitmap doInBackground(String... args) {
             try {
                   bm = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                  e.printStackTrace();
            }
          return bm;
           }
           protected void onPostExecute(Bitmap image) {
             if(image != null){
               prof_pic.setImageBitmap(image);
    //             pDialog.dismiss();
             }else{
    //             pDialog.dismiss();
    //             Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
             }
           }
       }
}
