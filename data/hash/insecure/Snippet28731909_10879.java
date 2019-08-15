public class SignupActivity extends ActionBarActivity{

private static final String TAG = "Custom debug";
private UiLifecycleHelper uiHelper;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.test.testApp",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    setContentView(R.layout.activity_signup);

    LoginButton fbAuthBtn = (LoginButton) findViewById(R.id.fbSignupBtn);
    fbAuthBtn.setReadPermissions(Arrays.asList("public_profile","email"));

    uiHelper = new UiLifecycleHelper(this, callback);
    uiHelper.onCreate(savedInstanceState);



}

@Override
protected void onResume() {
    super.onResume();

    // Logs 'install' and 'app activate' App Events.
    AppEventsLogger.activateApp(this);
}

@Override
protected void onPause() {
    super.onPause();

    // Logs 'app deactivate' App Event.
    AppEventsLogger.deactivateApp(this);
}

@Override
public void onDestroy() {
    super.onDestroy();
    uiHelper.onDestroy();
}


//Facebook methods
private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    if (state.isOpened()) {
        Log.i(TAG, "Logged in...");
    } else if (state.isClosed()) {
        Log.i(TAG, "Logged out...");
    }
}

private Session.StatusCallback callback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state, Exception exception) {
        onSessionStateChange(session, state, exception);
    }
};

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
   uiHelper.onActivityResult(requestCode, resultCode, data);
    //Session.getActiveSession().onActivityResult(SignupActivity.this, requestCode, resultCode, data);



    Log.i(TAG, "Request code is " + requestCode + " ResultCode is " + resultCode + " And the data is " + data.getDataString());

    if(resultCode == RESULT_OK){
        Bundle bundle = data.getExtras();

        String fbData = bundle.toString();
        System.out.println("I am inside resultcode " +fbData);
    }else
    {
        System.out.println("I have no idea what is happening :(");
    }
}

@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    uiHelper.onSaveInstanceState(outState);
}
