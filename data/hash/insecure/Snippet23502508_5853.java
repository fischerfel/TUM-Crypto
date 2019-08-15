@SuppressLint("NewApi")
public static void getApplicationSignature(Activity activity){
    try {
        PackageInfo info = activity.getPackageManager().getPackageInfo("your.package.name.here", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
