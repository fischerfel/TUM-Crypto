please try this. if it does not work.. uncomment commented code and try once.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.fbtest", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        loginButton = (LoginButton) findViewById(R.id.login_button);
         // Callback registration
        loginButton.registerCallback(callbackManager, new     FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                 userId = loginResult.getAccessToken().getUserId();
                 token = loginResult.getAccessToken().getToken();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });    
    }
