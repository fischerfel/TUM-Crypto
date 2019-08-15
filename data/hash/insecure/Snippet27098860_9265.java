public class MainActivity extends Activity {
    private static final String TAG = "FacebookConnect";
    LoginButton loginButton;
    private boolean isLoggedIn = false; // by default assume not logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isLoggedIn()) {

            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }

        loginButton = (LoginButton) findViewById(R.id.authB);
        loginButton.setReadPermissions(Arrays.asList("user_likes",
                "user_status", "user_location", "email", "user_birthday"));

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.can.fun", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {
                Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_LONG)
                        .show();
                onSessionStateChange(session, state, exception);
            }
        };

    }

    private void onSessionStateChange(Session session, SessionState state,
            Exception exception) {
        if (state.isOpened()) {
            // Get the user's data.

            // Request user data and show the results
            Request.newMeRequest(session, new Request.GraphUserCallback() {

                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    Toast.makeText(getBaseContext(), "hello3",
                            Toast.LENGTH_LONG).show();
                    if (user != null) {
                        // Display the parsed user info
                        Toast.makeText(getBaseContext(),
                                "" + buildUserInfoDisplay(user),
                                Toast.LENGTH_LONG).show();

                        Log.d("User Data", "" + buildUserInfoDisplay(user));
                    }
                }
            }).executeAsync();

        } else if (session.isOpened()) {
        }
    }

    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.isOpened());
    }

    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Name: %s\n\n", user.getName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        userInfo.append(String.format("Birthday: %s\n\n", user.getBirthday()));

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
        userInfo.append(String.format("Location: %s\n\n", user.getLocation()
                .getProperty("name")));

        // Example: access via property name (locale)
        // - no special permissions required
        userInfo.append(String.format("Locale: %s\n\n",
                user.getProperty("locale")));

        // Example: access via key for array (languages)
        // - requires user_likes permission
        JSONArray languages = (JSONArray) user.getProperty("languages");
        if (languages.length() > 0) {
            ArrayList<String> languageNames = new ArrayList<String>();
            for (int i = 0; i < languages.length(); i++) {
                JSONObject language = languages.optJSONObject(i);
                // Add the language name to a list. Use JSON
                // methods to get access to the name field.
                languageNames.add(language.optString("name"));
            }
            userInfo.append(String.format("Languages: %s\n\n",
                    languageNames.toString()));
        }

        return userInfo.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);

    }

}
