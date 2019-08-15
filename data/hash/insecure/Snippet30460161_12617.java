public class SampleApplication extends Application {

public void onCreate(){   
FacebookSdk.sdkInitialize(getApplicationContext());
printKeyHash();
}
 public void printKeyHash() {
try {
    PackageInfo info = getPackageManager().getPackageInfo("your package name", PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.e("Hash Key", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
    } catch (PackageManager.NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
}
}
