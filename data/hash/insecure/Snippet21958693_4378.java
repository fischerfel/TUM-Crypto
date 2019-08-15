try {
  PackageInfo info = getPackageManager().getPackageInfo("com.facebook.login", PackageManager.GET_SIGNATURES);
  for (Signature signature : info.signatures) {
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(signature.toByteArray());
    Log.d("KeyHash:", Global.debug + " " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
  }
} catch (NameNotFoundException e) {
  Log.d("Name Not Found", Global.debug);
} catch (NoSuchAlgorithmException e) {
  Log.d("No Such Algorithm", Global.debug);
}
