public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    Button loginfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        loginfb = (Button) findViewById(R.id.loginfb);
        loginfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_photos", "user_friends"));
            }
        });

        // Generate HasKey & Add this HashKey in Your Developer Account
        //getHashkey();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        // App code

                        Log.d("", "onSuccess: " + loginResult.getRecentlyGrantedPermissions());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback()
                                {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response)
                                    {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try
                                        { 
                                            String id = object.getString("id");
                                            String email = object.getString("email");

                                            Log.d("", "email: " + email);
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,link,picture,friends");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel()
                    {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        // App code
                        Log.d("", "onError: "+exception);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getHashkey()
    {
        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo("YOU PACKAGE NAME", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }
    }

}
