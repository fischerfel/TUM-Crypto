public class main extends Activity {

    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    @SuppressWarnings("unused")
    private boolean pendingPublishReauthorization = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

              //this code was for test
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "mypackage here", 
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }

        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        buttonLoginLogout = (ImageView)findViewById(R.id.buttonLoginLogout);
        post = (ImageButton)findViewById(R.id.fbshare);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();

post.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                 publishStory();
            }
        });
}
@Override
        public void onStart() {
            super.onStart();

            Session.getActiveSession().addCallback(statusCallback);
        }

        @Override
        public void onStop() {
            super.onStop();
            Session.getActiveSession().removeCallback(statusCallback);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Session session = Session.getActiveSession();
            Session.saveSession(session, outState);
        }

        private void updateView() {
            Session session = Session.getActiveSession();
            if (session.isOpened()) {
                buttonLoginLogout.setImageResource(R.drawable.fblogout);
                buttonLoginLogout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) { onClickLogout(); }
                });
                post.setVisibility(View.VISIBLE);
            } else {
                buttonLoginLogout.setImageResource(R.drawable.fblogin);
                buttonLoginLogout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) { onClickLogin(); }
                });
                post.setVisibility(View.INVISIBLE);
            }
        }

        private void onClickLogin() {
            Session session = Session.getActiveSession();
            if (!session.isOpened() && !session.isClosed()) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            } else {
                Session.openActiveSession(this, true, statusCallback);
            }

        }

        private void onClickLogout() {
            Session session = Session.getActiveSession();
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }

        }

        private class SessionStatusCallback implements Session.StatusCallback {
            public void call(Session session, SessionState state, Exception exception) {
                updateView();
            }
        }
        private void publishStory() {
            Session session = Session.getActiveSession();

            if (session != null){

                // Check for publish permissions    
                List<String> permissions = session.getPermissions();
                if (!isSubsetOf(PERMISSIONS, permissions)) {
                    pendingPublishReauthorization = true;
                    Session.NewPermissionsRequest newPermissionsRequest = new Session
                            .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                    return;
                }


                Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), messege, new Request.Callback() {
                    public void onCompleted(Response response) {

                        Toast.makeText(main.this, "messege sent..", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestAsyncTask task = new RequestAsyncTask(request);
                task.execute();
            }

        }
        private boolean isSubsetOf(Collection<String> subset, Collection<String>                                             superset) {
            for (String string : subset) {
                if (!superset.contains(string)) {
                    return false;
                }
            }
            return true;
        }
}
