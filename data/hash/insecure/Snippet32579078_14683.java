try {
    PackageInfo info = getPackageManager().getPackageInfo(
            "com.play.fabin",  //Replace your package name here
            PackageManager.GET_SIGNATURES);

    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        System.out.println("key hash = " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (PackageManager.NameNotFoundException e) {
    e.printStackTrace();

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
