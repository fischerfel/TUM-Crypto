public class ApiController extends Application {

public static final String TAG = ApiController.class.getSimpleName();

private RequestQueue mRequestQueue;
private ImageLoader mImageLoader;

private static ApiController mInstance;

@Override
public void onCreate() {
    super.onCreate();
    mInstance = this;
    /*FacebookSdk.sdkInitialize(getApplicationContext());
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.example.android.facebookloginsample",  // replace with your unique package name
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }*/

}

public static synchronized ApiController getInstance() {
    return mInstance;
}

public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return mRequestQueue;
}

public <T> void addToRequestQueue(Request<T> req, String tag) {
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
}

public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
}

public void cancelPendingRequests(Object tag) {

    if (mRequestQueue != null) {
        mRequestQueue.cancelAll(tag);
    }

}
