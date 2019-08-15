// Add code to print out the key hash
try {
    PackageInfo info = getPackageManager().getPackageInfo(
            "com.facebook.samples.hellofacebook", //your unique package name here
            PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));// this line  gives your keyhash
        }
} catch (NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
