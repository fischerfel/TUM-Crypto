public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();
    }

    public void printHashKey(){

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.parakhidevelopers.happydays",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Key Hash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
