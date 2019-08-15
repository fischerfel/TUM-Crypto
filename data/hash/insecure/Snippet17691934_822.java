public static void showHashKey(Context context) {
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.example.project", PackageManager.GET_SIGNATURES); //Your package name here
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.v("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
}
