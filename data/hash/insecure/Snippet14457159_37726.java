  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        if (APP_ID == null) {
        Util.showAlert(this, "Warning", "Facebook Application ID must be specified before running");
    }
    try {
       PackageInfo info = getPackageManager().getPackageInfo("com.ff.fbin", PackageManager.GET_SIGNATURES);
       for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("TAG", "Reverse FB key: "+Base64Coder.encodeLines(md.digest()));
       }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }



    mLoginButton = (LoginButton) findViewById(R.id.login);
    mFacebook = new Facebook(APP_ID);
    mAsyncRunner = new AsyncFacebookRunner(mFacebook);
    SessionStore.restore(mFacebook, this);
    SessionEvents.addAuthListener(new SampleAuthListener());
    SessionEvents.addLogoutListener(new SampleLogoutListener());
    mLoginButton.init(this, mFacebook, mPermissions);
 }


@Override
protected void onActivityResult(int requestCode, int resultCode,
                                Intent data) {
    mFacebook.authorizeCallback(requestCode, resultCode, data);

    boolean isLogined = mFacebook.isSessionValid();
    Log.i(TAG, "getAccessToken, onActivity:" +mFacebook.getAccessToken());

    if (isLogined){
        mAsyncRunner.request("me", new SampleRequestListener());
    }
}

public class SampleRequestListener extends BaseRequestListener {

    @Override
    public void onComplete(final String response, final Object state) {
        try {
            Log.d(TAG, "Response2: " + response.toString());
            JSONObject json = Util.parseJson(response);

            //Retrieve user information from facebook

final Runnable mUpdateResultsSuccess = new Runnable() {
    @Override
    public void run() {
        updateResultsInUiSuccess();
    }
};

private void updateResultsInUiSuccess() {
        if (loginSuccess){
            Log.i(TAG, "Login FB done");
            finish();
            Intent intent2 = new Intent(LoginScreen.this, MainMenu.class);
            helper.insert_user(mFacebook.getAccessToken());
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);

        } else {
            loading_text.setText("Error Processing");
            Log.i(TAG, "Login FB failed");
            try {
                mFacebook.logout(this);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "MalformedURLException: "+e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "IOException: "+e.getMessage());
            }
            SessionStore.clear(this);
            pDialog.dismiss();
            Toast.makeText(LoginScreen.this, "Login Failed. Please try again", Toast.LENGTH_LONG).show();
        }
}


public class SampleAuthListener implements com.ff.fbin.SessionEvents.AuthListener {

    @Override
    public void onAuthSucceed() {
        Log.i(TAG, "You have logged in! ");

        boolean isLogined = mFacebook.isSessionValid();
        Log.i(TAG, "isLogined: "+isLogined);

        if(!isFinishing()){

        };

        if (isLogined){
            mAsyncRunner.request("me", new SampleRequestListener());
            pDialog.setMessage("Login in process......");
            pDialog.show();
        }

    }

    @Override
    public void onAuthFail(String error) {
        Log.i(TAG, "Login Failed: " + error);
        Toast.makeText(getApplicationContext(), "Login Failed: " + error, Toast.LENGTH_LONG).show();
        pDialog.dismiss();
    }
}

public class SampleLogoutListener implements com.ff.fbin.SessionEvents.LogoutListener {
    @Override
    public void onLogoutBegin() {
        Log.i(TAG, "Logging out...");
    }

    @Override
    public void onLogoutFinish() {
        Log.i(TAG, "You have logged out! ");
    }
}
