private void facebookHashKey() {

    try {
        PackageInfo info = getPackageManager().getPackageInfo("com.app.helpcove", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String hashCode  = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            System.out.println("Print the hashKey for Facebook :"+hashCode);
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
