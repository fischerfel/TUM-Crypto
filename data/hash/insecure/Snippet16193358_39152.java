private void getShaKey() {

 try {
 PackageInfo info = getPackageManager().getPackageInfo("YOUR.Package.Name",
 PackageManager.GET_SIGNATURES);
 for (Signature signature : info.signatures) {
 MessageDigest md = MessageDigest.getInstance("SHA");
 md.update(signature.toByteArray());
 Log.v(TAG, "KeyHash:" + Base64.encodeToString(md.digest(),
 Base64.DEFAULT));
 }
 } catch (NameNotFoundException e) {
 e.printStackTrace();
 } catch (NoSuchAlgorithmException e) {
 e.printStackTrace();
 }

 }
