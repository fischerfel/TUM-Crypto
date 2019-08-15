private void getHashKey() {
try {
    PackageInfo info = getPackageManager().getPackageInfo(
            getPackageName(), PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.e("MY_KEY_HASH:",
                Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (NameNotFoundException e) {
} catch (NoSuchAlgorithmException e) {
} }
