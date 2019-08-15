try {
    PackageInfo info = getPackageManager().getPackageInfo("com.myapp", PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (PackageManager.NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
