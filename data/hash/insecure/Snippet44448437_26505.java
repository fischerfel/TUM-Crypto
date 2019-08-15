public static String getKeyHash(final Context context) {
    PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
    if (packageInfo == null)
        return null;

    for (Signature signature : packageInfo.signatures) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
        }
    }
    return null;
}
