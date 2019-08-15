private Button facebookBtn, signUp;
    private ImageView menu;
    private FBLoginManager fbLoginManager;
//  private EditText emailEt, passwordEt;
    private SlidingMenu slidingMenu;
    private User user;
//  private CheckBox licence;
    private int userID;
    String test;

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    private SignInButton signupButtonPlus;

    public final String KODEFUNFBAPP_ID = "736233383069948";

    static final String[] SCOPES = new String[] { Scopes.PLUS_PROFILE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities("http://schemas.google.com/AddActivity",
                        "http://schemas.google.com/BuyActivity")
                .setScopes(SCOPES) // Space separated list of scopes
                .build();
        initWidgets();
    }

    private void initWidgets() {
//      licence = (CheckBox) findViewById(R.id.signup_checkbox);
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        signupButtonPlus = (SignInButton) findViewById(R.id.sign_in_button_plus);
        signupButtonPlus.setOnClickListener(this);
        facebookBtn = (Button) findViewById(R.id.signup_fb);
        facebookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToFacebook();

            }
        });

    /*  emailEt = (EditText) findViewById(R.id.signup_email_et);
        passwordEt = (EditText) findViewById(R.id.signup_pass_et);*/
        /*signUp = (Button) findViewById(R.id.signup_new_user);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEt.getText().toString();
                final String pass = passwordEt.getText().toString();
                if (validateInput(email, pass)) {
                    try {
                        saveUserData("", "", email, "", pass);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });*/

        menu = (ImageView) findViewById(R.id.signup_menu);
        displayMenu();
    }

    private void registerNewUser(final String email, final String pass,
            final String username, final String imgURL, final String name,
            final int userId) throws IOException {
        final String registerURL = "http://myurl.com/gamer/admin2/mobile/user.php?action=register&email="
                + email
                + "&username="
                + username
                + "&password="
                + pass
                + "&img_url=" + imgURL;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(registerURL);

                // httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Accept",
                        "application/x-www-form-urlencoded");
                // httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Content-Type",
                        "application/x-www-form-urlencoded");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        4);

                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs
                        .add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                nameValuePairs.add(new BasicNameValuePair("img_url", imgURL));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                            "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    test = EntityUtils.toString(entity);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    JSONObject jObject = new JSONObject(test.substring(38,
                            test.length() - 1));
                    Log.v("--", jObject.toString());
                    userID = jObject.getInt("id");
                    SharedPreferences prefs = getSharedPreferences(
                            getString(R.string.app_package),
                            Context.MODE_PRIVATE);
                    prefs.edit().putInt("user_id", userID).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();

    }

    private boolean validateInput(String email, String password) {
        if (email.length() <= 6) {
            Toast.makeText(this, "Password must be at least 6 characters long",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmailAddress(email)) {
            Toast.makeText(this, "Enter valid email address",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if (!licence.isChecked()) {
            Toast.makeText(this, "Do you accept the license agreement",
                    Toast.LENGTH_SHORT).show();
            return false;
        }*/
        else
            return true;
    }

    public boolean isValidEmailAddress(String email) {
        java.util.regex.Pattern p = java.util.regex.Pattern
                .compile(".+@.+\\.[a-z]+");
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void displayMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            super.onBackPressed();
        }
    }

    public void connectToFacebook() {

        // read about Facebook Permissions here:
        // http://developers.facebook.com/docs/reference/api/permissions/
        String permissions[] = { "user_about_me", "user_activities",
                "user_birthday", "user_checkins", "user_education_history",
                "user_events", "user_groups", "user_hometown",
                "user_interests", "user_likes", "user_location", "user_notes",
                "user_online_presence", "user_photo_video_tags", "user_photos",
                "user_relationships", "user_relationship_details",
                "user_religion_politics", "user_status", "user_videos",
                "user_website", "user_work_history", "email",

                "read_friendlists", "read_insights", "read_mailbox",
                "read_requests", "read_stream", "xmpp_login", "ads_management",
                "create_event", "manage_friendlists", "manage_notifications",
                "offline_access", "publish_checkins", "publish_stream",
                "rsvp_event", "sms", "publish_actions"

        };

        fbLoginManager = new FBLoginManager(this, R.layout.signup,
                KODEFUNFBAPP_ID, permissions);

        if (fbLoginManager.existsSavedFacebook()) {
            fbLoginManager.loadFacebook();
        } else {
            fbLoginManager.login();
        }
    }

    @Override
    public void loginSuccess(final Facebook facebook) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                GraphApi graphApi = new GraphApi(facebook);
                user = new User();
                try {
                    user = graphApi.getMyAccountInfo();

                    // update your status if logged in / post on wall
                    // graphApi.setStatus("Hello, world!");
                } catch (EasyFacebookError e) {
                    Log.d("TAG: ", e.toString());
                }

                return null;
            }

            protected void onPostExecute(Void result) {
                try {
                    saveUserData(user.getFirst_name(), user.getName(),
                            user.getEmail(), "https://graph.facebook.com/"
                                    + user.getId() + "/picture?type=large",
                            hash(user.getId()).toString());
                    registerNewUser(user.getEmail(), hash(user.getId())
                            .toString(), user.getFirst_name(),
                            "https://graph.facebook.com/" + user.getId()
                                    + "/picture?type=large", user.getName(),
                            userID);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            };

        }.execute();

    }

    public byte[] hash(String password) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] passBytes = password.getBytes();
        byte[] passHash = sha256.digest(passBytes);
        Log.v("--", "pass " + passHash.toString());
        return passHash;
    }

    private void saveUserData(String username, String name, String email,
            String imageURL, String pass) throws IOException {
        registerNewUser(email, pass, username, imageURL, name, userID);
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.app_package), Context.MODE_PRIVATE);
        prefs.edit().putString("user_fullname", name).commit();
        prefs.edit().putString("user_username", username).commit();
        prefs.edit().putString("user_email", email).commit();
        prefs.edit().putInt("user_id", userID).commit();
        prefs.edit().putString("user_picture", imageURL).commit();
        prefs.edit().putBoolean("user_logedin", true).commit();
        Intent i = new Intent(SignUp.this, Profile.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub
        if (result.hasResolution()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.disconnect();
                mPlusClient.connect();
            }
        }
    }

    @Override
    public void logoutSuccess() {
        fbLoginManager.displayToast("Logout Success!");
    }

    @Override
    public void loginFail() {
        fbLoginManager.displayToast("Login Epic Failed!");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button_plus) {
            Log.v("--", "gplus");
            if (!mPlusClient.isConnected()) {
                mPlusClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            android.content.Intent data) {
        Log.v("--", resultCode + " fb");
        Log.v("--", "req code "+requestCode);

        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
            mConnectionResult = null;
            Log.v("--", resultCode + " G+");
            mPlusClient.connect();
        } else
            fbLoginManager.loginSuccess(data);
    }

    @Override
    public void onDisconnected() {
        Log.e("gPlus", "disconnected");
    }

    @Override
    public void onConnected(Bundle b) {
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG)
                .show();
    }
