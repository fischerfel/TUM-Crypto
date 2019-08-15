   
public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        facebookOncreateCalling();

        Button loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoginManager.getInstance().logOut();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_about_me"));
            }
        });

       
 // Here I want to open facebook profile page in browser
        Button openWeb = (Button)findViewById(R.id.openWeb);
        openWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                //String facebookUrl = "http://www.facebook.com/"+fbid;

                String facebookUrl = "https://www.facebook.com/profile.php?id=" + fbid;

                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);*/

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/androiddevs"));
                    startActivity(intent);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        });

    }

////////////////////////////// Facebook login starts //////////////////////////////////////
    private void facebookOncreateCalling() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Facebook_KeyHash:", "KeyHash:-> " + Base64.encodeToString(md.digest(), Base64.DEFAULT));


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("sammy_accessToken "+loginResult.getAccessToken().getToken());

                        callGraphApi(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("====Login Activity===","Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("====Login Activity===","Error"+exception);
                    }
                });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
    }

    private void callGraphApi(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            System.out.println("SAM_output "+object);
                           String fbUserId = object.optString("id");
                           String firstName = object.optString("first_name");
                           String lastName = object.optString("last_name");
                           String eMail = object.optString("email");

                           System.out.println("SAM_ID:" + " " + fbUserId);
                           System.out.println("SAM_First_Name:" + " " + firstName);
                           System.out.println("SAM_Last_Name:" + " " + lastName);
                           System.out.println("SAM_Email:" + " " + eMail);

                            fbid = fbUserId;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,birthday,gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
////////////////////////////// Facebook login ends ////////////////////////////////////////
}