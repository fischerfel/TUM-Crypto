public static String getKeyHash(Context context) {
    String returner = "";
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.abc.mypackage", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            returner = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.e(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
        Log.e(TAG, e.toString());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        Log.e(TAG, e.toString());

    }
    return returner;
}
