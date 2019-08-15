try {
    PackageInfo info = getPackageManager().getPackageInfo(
        "My Project", 
        PackageManager.GET_SIGNATURES);

    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
        Toast.makeText(this, Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
    }
} catch (NameNotFoundException e) {
} catch (NoSuchAlgorithmException e) {
}
