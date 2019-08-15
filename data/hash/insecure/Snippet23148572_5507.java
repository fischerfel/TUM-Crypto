public class FacebookLogin3Activity extends BaseActivity {
    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state,
                Exception exception) {

            // String hashNew = getHash();

            onSessionStateChange(session, state, exception);
            if (session.isOpened()) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Log.e("user", "session established");
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            Log.e("user", "loged user");
                            // buildUserInfoDisplay(user);
                        }
                    }
                }).executeAsync();
            } else {
                Log.e("user", "session not established");
            }
        }

        private void onSessionStateChange(Session session, SessionState state,
                Exception exception) {

        }
    };

    private String getHash() {
        PackageInfo info;
        String hash = "no data";

        try {

            info = getPackageManager()
                    .getPackageInfo("com.example.manyexampleapp",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return hash;
    }

    private UiLifecycleHelper uiHelper; // and in the on create method to use
                                        // the login button

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // StrictMode.ThreadPolicy policy = new
        // StrictMode.ThreadPolicy.Builder()
        // .permitAll().build();

        // StrictMode.setThreadPolicy(policy);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_login);

        LoginButton buttonLoginLogout = (LoginButton) findViewById(R.id.authButton);
        buttonLoginLogout.setReadPermissions(Arrays.asList("user_status"));

    }

    // also need to manage the ui status in al lifecycle methods

    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
