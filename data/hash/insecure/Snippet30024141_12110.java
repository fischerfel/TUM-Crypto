public class MyApplication extends Application {
@Override
public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(getApplicationContext());
}

/**
 * Call this method inside onCreate once to get your hash key
 */
public void printKeyHash() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo("vivz.slidenerd.facebookv40helloworld", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("VIVZ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
