       @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();

    setContentView(R.layout.activity_register_skip);

    skip = (TextView) findViewById(R.id.textViewSkip);
    register = (Button) findViewById(R.id.buttonRegisterOne);

    skip.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SessionHandler handler = new SessionHandler(RegisterSkipActivity.this.getApplicationContext());
            handler.storeLoginSession("guest");
            SharedPreferences.Editor editor = getSharedPreferences(
                    "MyPref", MODE_PRIVATE).edit();
            String p_name ="guest";
            editor.putString("personName", p_name);
            editor.putString("TAG", "guest");
            editor.commit();
            Intent intent = new Intent(RegisterSkipActivity.this, MainActivity.class);
            intent .putExtra("username","Guest");
            startActivity(intent);
        }
    });

    register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });
    /////fb and g+
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.techieweb.solutions.pickeronline",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    loginButton = (LoginButton) findViewById(R.id.login_button);
    btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();
    btnSignIn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn();
        }
    });
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            type = "1";
loginResult.getAccessToken().getToken());
            Log.e("In onSuccess","onsuccess");
            Intent intent = new Intent(RegisterSkipActivity.this, MainActivity.class);
            intent.putExtra("Login", type);
            startActivity(intent);

        }


        @Override
        public void onCancel() {
            // info.setText("Login attempt cancelled.");
            Log.e("In onCancel","oncancel");
        }

        @Override
        public void onError(FacebookException e) {
            //info.setText("Login attempt failed.");
            Log.e("In onError","onerror");
        }
    });

} 
