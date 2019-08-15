private void printKeyHash() {
    // Add code to print out the key hash
    try {
        PackageInfo info = getPackageManager().getPackageInfo("**YOUR PACKAGE NAME**", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.e("KeyHash:", e.toString());
    } catch (NoSuchAlgorithmException e) {
        Log.e("KeyHash:", e.toString());
    }
}
