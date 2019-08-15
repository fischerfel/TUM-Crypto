public class Login extends AppCompatActivity {
    private String eml;
    private String pswrd;
    private ProfileTracker mProfileTracker;
    private ProgressDialog pDialog;
    String status = "";
    private Button fbbutton;
    Profile profile;
    Button login;

    // private int serverResponseCode = 0;
    TextView tac1;
    EditText email, pass;
    private static String url_create_book = "http://cloud.....com/broccoli/login.php";
    public static CallbackManager callbackmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        Get_hash_key();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





       // AppEventsLogger.activateApp(this);

        fbbutton = (Button) findViewById(R.id.fbtn);

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });

        email = (EditText)findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        tac1 = (TextView)findViewById(R.id.cAcc);

        tac1.setOnClickListener(new View.OnClickListener()

                                {

                                    @Override
                                    public void onClick(View v) {


                                        startActivity(new Intent(Login.this, RegistrationForm.class));


                                    }
                                }

        );

        login = (Button) findViewById(R.id.lbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pDialog = new ProgressDialog(Login.this);
                pDialog.setMessage("Please wait..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                eml = email.getText().toString();
                pswrd = pass.getText().toString();


                // new CreateNewProduct().execute();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_create_book,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismiss();
                                if (response.trim().equals("success")) {
                                    Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, Home.class));
                                    //your intent code here
                                } else {
                                    Toast.makeText(Login.this, "username/password incorrect", Toast.LENGTH_SHORT).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", eml);
                        params.put("password", pswrd);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);

            }

        });
    }


    public void Get_hash_key() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.zeba.broccoli", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }




    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        try {
                            if (Profile.getCurrentProfile() == null) {
                                mProfileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile profile_old, Profile profile_new) {
                                        // profile2 is the new profile
                                        profile = profile_new;
                                        mProfileTracker.stopTracking();
                                    }
                                };
                                mProfileTracker.startTracking();
                            } else {
                                profile = Profile.getCurrentProfile();
                            }

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("FACEBOOK LOGIN", response.toString());
                                            // Application code
                                            try {
                                                String fb_id = object.getString("id");
                                                String fb_name = object.getString("name");
                                                String profilePicUrl = "https://graph.facebook.com/" + fb_id + "/picture?width=200&height=200";
                                                String fb_gender = object.getString("gender");
                                                String fb_email = object.getString("email");
                                                String fb_birthday = object.getString("birthday");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            //use shared preferences here
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday,picture.type(small)");
                            request.setParameters(parameters);
                            request.executeAsync();


                            //go to Home page
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Log.d("ERROR", e.toString());
                        }
                    }


                    @Override
                    public void onCancel() {
                        // Log.d(TAG_CANCEL, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Log.d(TAG_ERROR, error.toString());
                    }



                    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                        callbackmanager.onActivityResult(requestCode, resultCode, data);
                    }
                });




    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
