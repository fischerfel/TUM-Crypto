public static String getAppKeyHash(Context context) {
    // Add code to print out the key hash
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.example.app", PackageManager.GET_SIGNATURES);
        String hash = null;
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.d("KeyHash:", hash);
        }
        return hash;
    } catch (NameNotFoundException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    }
}
