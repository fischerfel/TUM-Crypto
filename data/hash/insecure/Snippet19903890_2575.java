public static String getKeyHash(Context context) {
    String returner = "";
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.lochmann.fb.viergewinntmultiplayer", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            returner = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.e("MY KEY HASH:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {
        e.printStackTrace();
        Log.e("ERROR", "NO NAME");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        Log.e("ERROR", "NO ALGO");

    }
    return returner;
}
