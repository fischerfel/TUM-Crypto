public void generateFbFingerPrint() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.group3amd.gc.activity",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String sign = Base64
                    .encodeToString(md.digest(), Base64.DEFAULT);
            Log.e("KEYHASH:", sign);
            Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG)
                    .show();
        }
    } catch (NameNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }




}
