@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(this.getApplicationContext());
    setContentView(R.layout.activity_main);
    callbackManager = CallbackManager.Factory.create();
    loginButton = (LoginButton) findViewById(R.id.login_button);
    List<String> permissionNeeds = Arrays.asList("public_profile", "user_friends");
    loginButton.setReadPermissions(permissionNeeds);

    try {
        PackageInfo info = getPackageManager().getPackageInfo("com.example.ryans_000.facebooklogin", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


        @Override
        public void onSuccess(LoginResult loginResult) {
            System.out.println("onSuccess");
        }

        @Override
        public void onCancel() {
            System.out.println("onCancel");
        }

        @Override
        public void onError(FacebookException exception) {
            Log.v("LoginActivity", exception.getCause().toString()); }
    });
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
}}
