public class MainActivity extends ActionBarActivity {

private CallbackManager callbackManager;
private LoginButton loginButton;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();
    getFbKeyHash("org.code2care.fbloginwithandroidsdk");

    setContentView(R.layout.activity_main);
    loginButton = (LoginButton)findViewById(R.id.login_button);

    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            System.out.println("Facebook Login Successful!");
            System.out.println("Logged in user Details : ");
            System.out.println("--------------------------");
            System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
            System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
            System.out.println("Facebook Login failed!!");

        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
            System.out.println("Facebook Login failed!!");
        }
    });
}

public void getFbKeyHash(String packageName) {

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            System.out.println("YourKeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

}

@Override
protected void onActivityResult(int reqCode, int resCode, Intent i) {
    callbackManager.onActivityResult(reqCode, resCode, i);
}
