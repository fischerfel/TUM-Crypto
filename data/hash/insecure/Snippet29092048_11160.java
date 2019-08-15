public static void showHashKey(Context context) {
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.example.me", PackageManager.GET_SIGNATURES); //Your package name here
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
}
