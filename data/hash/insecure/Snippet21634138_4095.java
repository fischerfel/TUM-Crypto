public class Login {
    private final static String TAG = "FaceBookLogin";
    public Context ctx;
    public Session fb_session;

    public Login(Context _ctx) {
        ctx = _ctx;
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        fb_session = Session.getActiveSession();

        if(fb_session == null)                   
            fb_session = Session.openActiveSessionFromCache(ctx);
    }

    public void checkLogin() {
        printHashKey();
        if (fb_session != null && fb_session.isOpened()) {
            Log.i(TAG, "Facebook Login State");
        } else {
            if (fb_session == null)
                fb_session = new Session(ctx);

            Session.setActiveSession(fb_session);
            ConnectToFacebook();
            Log.i(TAG, "Facebook Not login State");
        }
    }

    public void printHashKey() {

        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo("com.project.hkseven",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG,
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
              Log.d(TAG,""+e);
        } catch (NoSuchAlgorithmException e) {
              Log.d(TAG,""+e);
        }

    }

    private void ConnectToFacebook() {
        Session session = Session.getActiveSession();

        if(session == null)                   
            session = Session.openActiveSessionFromCache(ctx);

        if (!session.isOpened() && !session.isClosed()) {
            Log.i(TAG, "ConnectToFacebook if");
            OpenRequest newSession = new Session.OpenRequest((Activity) ctx);
            newSession.setCallback(callback);
            session.openForRead(newSession);
            try {
                Session.OpenRequest request = new Session.OpenRequest((Activity) ctx);
                request.setPermissions(Arrays.asList("email","publish_stream","publish_actions"));
            } catch (Exception e) {
                Log.d(TAG,""+e);
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "ConnectToFacebook else");
            Session.openActiveSession((Activity) ctx, true, callback);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        public void call(final Session session, final SessionState state,
                final Exception exception) {
            Log.d(TAG,"callback" + state);
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(final Session session,
            SessionState state, Exception exception) {
        Log.i(TAG, "state change");
        if (session != null && session.isOpened()) {
            Log.i(TAG, "Change to Facebook Login");
        }
    }
}
