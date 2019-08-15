public void getHashkey(){
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());

            Log.i("Base64", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.d("Name not found", e.getMessage(), e);

    } catch (NoSuchAlgorithmException e) {
        Log.d("Error", e.getMessage(), e);
    }
}
