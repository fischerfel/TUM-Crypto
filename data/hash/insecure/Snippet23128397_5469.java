    public class MainActivity extends Activity {
        Facebook fb;
    Button login,getData,logout;    
    ImageView ig;
    String app_id;
    String access_token;
    long expires;
    private static AsyncFacebookRunner mAsyncRunner;

    private SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_main);
         app_id= getString(R.string.app_id);
         fb= new Facebook(app_id);
            login=(Button) findViewById(R.id.login);
            logout=(Button) findViewById(R.id.logout);
            getData=(Button) findViewById(R.id.getData);
            // ig= (ImageView) findViewById(R.id.profile_pic);
             login.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loginToFacebook();
                    }
                });

    getData.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            getProfileInformation();
        }
    });
         logout.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        if(fb.isSessionValid()){
            Session.initializeStaticContext(MainActivity.this.getApplicationContext());
            logoutFromFacebook();
        }

    }
    });
    mAsyncRunner = new AsyncFacebookRunner(fb);
    //updateButtonImage();
    try {

        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures)
        {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());
         Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

       } catch (NameNotFoundException e) {
        Log.e("name not found", e.toString());
       } catch (NoSuchAlgorithmException e) {
        Log.e("no such an algorithm", e.toString());
       }    
        }




        @SuppressWarnings("deprecation")
        public void loginToFacebook() {

            mPrefs = getPreferences(MODE_PRIVATE);
            access_token = mPrefs.getString("access_token", null);
         expires = mPrefs.getLong("access_expires", 0);

            if (access_token != null) {
                fb.setAccessToken(access_token);

                login.setVisibility(View.VISIBLE);

                // Making get profile button visible
                getData.setVisibility(View.VISIBLE);

                Log.d("FB Sessions", "" + fb.isSessionValid());
            }

            if (expires != 0) {
                fb.setAccessExpires(expires);
                Toast.makeText(MainActivity.this, "already login", Toast.LENGTH_LONG).show();}


            if (!fb.isSessionValid()) {
                fb.authorize(this,
                        new String[] { "email", "publish_stream" },
                        new DialogListener() {

                            @Override
                            public void onCancel() {
                                // Function to handle cancel event
                            }

                            @Override
                            public void onComplete(Bundle values) {
                                // Function to handle complete event
                                // Edit Preferences and update facebook acess_token
                             editor = mPrefs.edit();
                                editor.putString("access_token",
                                        fb.getAccessToken());
                                editor.putLong("access_expires",
                                        fb.getAccessExpires());
                                editor.commit();

                                // Making Login button invisible
                                login.setVisibility(View.VISIBLE);

                                // Making logout Button visible
                                getData.setVisibility(View.VISIBLE);


                            }

                            @Override
                            public void onError(DialogError error) {
                                // Function to handle error

                            }

                            @Override
                            public void onFacebookError(FacebookError fberror) {
                                // Function to handle Facebook errors

                            }

                        });
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            fb.authorizeCallback(requestCode, resultCode, data);
        }


        @SuppressWarnings("deprecation")
        public void getProfileInformation() {
            mAsyncRunner.request("me", new RequestListener() {
                public void onComplete(String response, Object state) {
                    Log.d("Profile", response);
                    String json = response;
                    try {
                        // Facebook Profile JSON data
                        JSONObject profile = new JSONObject(json);

                        // getting name of the user
                        final String name = profile.getString("name");

                        // getting email of the user
                        final String email = profile.getString("email");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();

                            }

                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onIOException(IOException e, Object state) {
                }

                public void onFileNotFoundException(FileNotFoundException e,
                        Object state) {
                }

                public void onMalformedURLException(MalformedURLException e,
                        Object state) {
                }

                public void onFacebookError(FacebookError e, Object state) {
                }
            });

        }


         @Deprecated
         public void logoutFromFacebook() {

                mAsyncRunner.logout(MainActivity.this, new RequestListener() {
                    @Override
                    public void onComplete(String response, Object state) {
                        Log.d("Logout from Facebook", response);
                        if (Boolean.parseBoolean(response) == true) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // make Login button visible
                                    login.setVisibility(View.VISIBLE);

                                    // making all remaining buttons invisible
                                    getData.setVisibility(View.INVISIBLE);

                                }

                            });

                        }
                    }

                    @Override
                    public void onIOException(IOException e, Object state) {
                    }

                    @Override
                    public void onFileNotFoundException(FileNotFoundException e,
                            Object state) {
                    }

                    @Override
                    public void onMalformedURLException(MalformedURLException e,
                            Object state) {
                    }

                    @Override
                    public void onFacebookError(FacebookError e, Object state) {
                    }
                });
            }

 @Override protected void onDestroy() {
        // TODO Auto-generated method stub
          super.onDestroy();
         // if(fb.isSessionValid()){
        Session.getActiveSession().closeAndClearTokenInformation();
        //  }
        }
    }
