// Add code to print out the key hash
  try {
  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
  for (Signature signature : info.signatures) {
  MessageDigest md = MessageDigest.getInstance("SHA");
  md.update(signature.toByteArray());
  Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
  } catch (NameNotFoundException e) {

  } catch (NoSuchAlgorithmException e) {

  }
