public class GenerateFacebookSignature extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
    }

    public void printHashKey() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.facebooklogin", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());

                Log.d("FaceBookKeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }
    }
} 
