public class main extends Activity {

    private UiLifecycleHelper uiLifeCycleHelper;
    private boolean isResumed = false;
    private ProfilePictureView profilePictureView;
    private TextView userNameView;
    private static final int REAUTH_ACTIVITY_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Log.d("YOSI", "" + this.getClass().getCanonicalName());

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.activity", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startPickerActivity(PickerActivity.FRIEND_PICKER, 1);

            }
        });

        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        userNameView = (TextView) findViewById(R.id.selection_user_name);
        profilePictureView.setCropped(true);

        uiLifeCycleHelper = new UiLifecycleHelper(this, new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {

                Log.v("log_tag", "Token=" + session.getAccessToken());
                Log.v("log_tag", "Token=" + session.isOpened());

                if (session.isOpened()) {
                    Log.d("SESSION", "Is Opened");
                    onSessionStateChange(session, state, exception);
                }


                try {
                    Log.d("EXCEPTION", "" + exception.getMessage());
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        uiLifeCycleHelper.onCreate(savedInstanceState);

    }

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                profilePictureView.setProfileId(user.getId());
                                userNameView.setText(user.getFirstName());
                                String json = user.getInnerJSONObject()
                                        .toString();
                                Log.d("JSON", json);
                            }
                        }

                        if (response.getError() != null) {

                        }

                    }
                });

        request.executeAsync();
    }

    private void onSessionStateChange(final Session session,
            SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);

    }

    private void startPickerActivity(Uri data, int requestCode) {
        Intent intent = new Intent();
        intent.setData(data);
        intent.setClass(this, PickerActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiLifeCycleHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiLifeCycleHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifeCycleHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifeCycleHelper.onSaveInstanceState(outState);
    }
}
