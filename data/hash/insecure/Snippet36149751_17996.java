public void generateHashkey(){
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                PACKAGE,
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());

            ((TextView) findViewById(R.id.package_name)).setText(info.packageName);
            ((TextView) findViewById(R.id.hash_key)).setText(Base64.encodeToString(md.digest(),
                                                              Base64.NO_WRAP));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.d(TAG, e.getMessage(), e);
    } catch (NoSuchAlgorithmException e) {
        Log.d(TAG, e.getMessage(), e);
    }
}
