try {
   PackageInfo info = getPackageManager().getPackageInfo("**YOURPACKAGENAME**", PackageManager.GET_SIGNATURES);
   for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.i("PXR", Base64.encodeBytes(md.digest()));
   }
}
catch (NameNotFoundException e) {}
catch (NoSuchAlgorithmException e) {}
