private BlankFragment mainFragment;
private LoginButton loginButton;
private CallbackManager callbackManager;


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_facebook);



    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();


    loginButton = (LoginButton) findViewById(R.id.login_button);

    printHashkey();

    if (savedInstanceState == null) {
        // Add the fragment on initial activity setup
        mainFragment = new BlankFragment();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, mainFragment).commit();
    } else {
        // Or set the fragment from restored state info
        mainFragment = (BlankFragment) getSupportFragmentManager()
                .findFragmentById(android.R.id.content);
    }



}




public void printHashkey(){
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "BLA BLA BLA BLA BLA BLA",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
