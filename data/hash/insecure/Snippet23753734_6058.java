private void printKeyHash(){
    // Add code to print out the key hash
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "YOUR_PACKAGE_NAME", 
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {
        Log.d("KeyHash:", e.toString());
    } catch (NoSuchAlgorithmException e) {
        Log.d("KeyHash:", e.toString());
    }
}
