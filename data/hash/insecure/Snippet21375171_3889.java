try {
    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (Exception e) {

}
