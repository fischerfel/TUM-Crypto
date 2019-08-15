public class FbManager {

private Context context;
private static final String LOGTAG = "FbManager";
private CallbackManager callbackManager;

public FbManager(Context context, CallbackManager callbackManager){
    this.context = context;
    this.callbackManager = callbackManager;
}

public static void traceKeyHash(Activity activity){
    try {
        PackageInfo info = activity.getPackageManager().getPackageInfo("com.muv.android", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i(LOGTAG, "Share - KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}

public void share(final String msg) {

    if (isLoggedIn()) {
        post(msg);
    }
    else{
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(LOGTAG, "facebook login success");
                post(msg);
            }

            @Override
            public void onCancel() {
                Log.w(LOGTAG, "facebook login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(LOGTAG, "facebook login error");
                exception.printStackTrace();
            }
        });

        LoginManager.getInstance().logInWithPublishPermissions((Activity) context, Arrays.asList("publish_actions"));
    }
}

public boolean isLoggedIn() {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    return accessToken != null;
}

private void post(final String msg){
    Log.d(LOGTAG, "facebook posting new message");
    Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();
    AccessToken accessToken = AccessToken.getCurrentAccessToken();

    Bundle postParams = new Bundle();
    postParams.putString("message", msg);

    GraphRequest request = new GraphRequest(accessToken, "me/feed", postParams, HttpMethod.POST, null);
    GraphRequestAsyncTask asynTaskGraphRequest = new GraphRequestAsyncTask (request);
    asynTaskGraphRequest.execute();
}
}
