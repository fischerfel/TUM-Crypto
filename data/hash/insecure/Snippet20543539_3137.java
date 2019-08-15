try {
    PackageInfo info = getPackageManager().getPackageInfo(
            "com.example.fishe", 
            PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("com.example.fishe", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
} catch (NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
