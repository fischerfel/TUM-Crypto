 try {
    PackageInfo info = getPackageManager().getPackageInfo(
            "your.application.package.name",
            PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (PackageManager.NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {
 }
