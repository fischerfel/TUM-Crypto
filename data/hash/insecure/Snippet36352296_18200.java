public class MainActivity extends AppCompatActivity implements GraphRequest.GraphJSONObjectCallback, GraphRequest.GraphJSONArrayCallback {
    private static final String TAG = "MainActivity";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    SharedPreferences sharedpreferences;
    User user;
    ArrayList<HashMap<String, String>> friends_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        generateHashkey();
        user=new User();
        callbackManager = CallbackManager.Factory.create();
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.toString());
                getUserDetails();
                getMyFriendsDetails();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, e.toString());
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            getUserDetails();
            getMyFriendsDetails();
        }
        Button bn=(Button)findViewById(R.id.button);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new connect().execute();

            }
        });
    }

    private void getUserDetails() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, this);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name, first_name, last_name, email,gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void getMyFriendsDetails() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name, first_name, last_name, email,gender, birthday, location");
            GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken, this);
            request.setParameters(parameters);
            request.executeAsync();
        }
    }


    /*private void getTaggableFriends(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Bundle bundle=new Bundle();
        bundle.putString("fields", "id, first_name, last_name, picture,email");
        if(accessToken!=null){
            GraphRequest graphRequest=new GraphRequest(accessToken,"/950173815028714/taggable_friends",null, HttpMethod.GET, new GraphRequest.Callback(){
                public void onCompleted(GraphResponse response) {
                    Log.d("Taggable friends",response.toString());
                }
            });
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.colors.fbfriends", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
        // Log.d("My friends:", jsonArray.toString());
    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Log.i("MainActivity",accessToken.getApplicationId());
        Log.i("MainActivity",accessToken.getToken());

        Log.i("MainActivity", accessToken.getUserId());

        Log.d("User Details:", jsonObject.toString());


        user.setAccessToken(accessToken.getToken());
        user.setApp_Id(accessToken.getApplicationId());
        user.setUserId(accessToken.getUserId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }




    private class connect extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... voids) {
            ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222);
            config.setSASLAuthenticationEnabled(true);
            XMPPConnection xmpp = new XMPPConnection(config);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                config.setTruststoreType("AndroidCAStore");
                config.setTruststorePassword(null);
                config.setTruststorePath(null);
            } else {
                config.setTruststoreType("BKS");
                String path = System.getProperty("javax.net.ssl.trustStore");
                if (path == null)
                    path = System.getProperty("java.home") + File.separator + "etc"
                            + File.separator + "security" + File.separator
                            + "cacerts.bks";
                config.setTruststorePath(path);
            }
            try
            {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                SASLAuthentication.registerSASLMechanism("X-FACEBOOK-PLATFORM", SASLXFacebookPlatformMechanism.class);
                SASLAuthentication.supportSASLMechanism("X-FACEBOOK-PLATFORM", 0);
                xmpp.connect();
                xmpp.login("879963405446307" ,accessToken.getToken(), "application");
            } catch (XMPPException e)
            {
                xmpp.disconnect();
                e.printStackTrace();
            }
             return null ;
        }


    }

    }



public class SASLXFacebookPlatformMechanism extends SASLMechanism {
    private static final String NAME = "X-FACEBOOK-PLATFORM";

    private String apiKey = "";
    private String accessToken = "";

    /**
     * Constructor.
     */
    public SASLXFacebookPlatformMechanism(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
    }

    @Override
    protected void authenticate() throws IOException, XMPPException {
        getSASLAuthentication().send(new AuthMechanism(NAME, ""));
    }

    @Override
    public void authenticate(String apiKey, String host, String accessToken)
            throws IOException, XMPPException {
        if (apiKey == null || accessToken == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        this.apiKey = apiKey;
        this.accessToken = accessToken;
        this.hostname = host;

        String[] mechanisms = { "DIGEST-MD5" };
        Map<String, String> props = new HashMap<String, String>();
        this.sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props,
                this);
        authenticate();
    }

    @Override
    public void authenticate(String username, String host, CallbackHandler cbh)
            throws IOException, XMPPException {
        String[] mechanisms = { "DIGEST-MD5" };
        Map<String, String> props = new HashMap<String, String>();
        this.sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props,
                cbh);
        authenticate();
    }

    @Override
    protected String getName() {
        return NAME;
    }

    @Override
    public void challengeReceived(String challenge) throws IOException {
        byte[] response = null;

        if (challenge != null) {
            String decodedChallenge = new String(Base64.decode(challenge));
            Map<String, String> parameters = getQueryMap(decodedChallenge);

            String version = "1.0";
            String nonce = parameters.get("nonce");
            String method = parameters.get("method");

            String composedResponse = "method="
                    + URLEncoder.encode(method, "utf-8") + "&nonce="
                    + URLEncoder.encode(nonce, "utf-8") + "&access_token="
                    + URLEncoder.encode(accessToken, "utf-8") + "&api_key="
                    + URLEncoder.encode(apiKey, "utf-8") + "&call_id=0" + "&v="
                    + URLEncoder.encode(version, "utf-8");
            response = composedResponse.getBytes();
        }

        String authenticationText = "";

        if (response != null) {
            authenticationText = Base64.encodeBytes(response);
        }

        // Send the authentication to the server
        getSASLAuthentication().send(new Response(authenticationText));
    }

    private Map<String, String> getQueryMap(String query) {
        Map<String, String> map = new HashMap<String, String>();
        String[] params = query.split("\\&");

        for (String param : params) {
            String[] fields = param.split("=", 2);
            map.put(fields[0], (fields.length > 1 ? fields[1] : null));
        }

        return map;
    }
}
