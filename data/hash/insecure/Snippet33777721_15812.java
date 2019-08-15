public class MainActivity extends Activity {

    private static String APP_ID = "1500167156975715"; // Replace your App ID here
    CallbackManager callbackManager;
    ArrayList<String> permissions;
    Context cv = this;
    boolean loginflag = false;
    private ProfileTracker profiletracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initilise fb sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        //logout any previous logins
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        accessToken.setCurrentAccessToken(null);
        Profile.getCurrentProfile().setCurrentProfile(null);
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_main);
        permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("user_likes");
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("user_birthday");


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.facebooklogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Parth", "KeyHash : " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginbutton = (LoginButton) findViewById(R.id.login_button);
        loginbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
                    if (loginflag) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        accessToken.setCurrentAccessToken(null);
                        Profile.getCurrentProfile().setCurrentProfile(null);
                        Toast.makeText(cv, "Logout", Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                    }
                } else {
                    if (!loginflag) {
                        Toast.makeText(cv, "Login", Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissions);
                    }
                }
            }
        });
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult result) {
                // TODO Auto-generated method stub
                Toast.makeText(cv, "in onSucess", Toast.LENGTH_SHORT).show();
                //get account details
                GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // TODO Auto-generated method stub
                        Log.d("Parth", response.toString() + "\njson" + object.toString());
                        try {
                            String email = (String) object.get("email");
                            Toast.makeText(cv, email, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                // TODO Auto-generated method stub
                Toast.makeText(cv, "in onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Toast.makeText(cv, "in oncancel", Toast.LENGTH_SHORT).show();
            }
        });
        profiletracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // TODO Auto-generated method stub
                if (currentProfile != null) {
                    Toast.makeText(cv, "in Currentprofilechanged", Toast.LENGTH_SHORT).show();
                    String name = currentProfile.getName();
                    String fname = currentProfile.getFirstName();
                    Toast.makeText(MainActivity.this, fname, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
