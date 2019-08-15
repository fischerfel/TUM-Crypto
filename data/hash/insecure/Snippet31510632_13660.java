   try {
       PackageInfo info = getPackageManager().getPackageInfo(
               "com.example.mypack", PackageManager.GET_SIGNATURES);
       for (android.content.pm.Signature signature : info.signatures) {
           MessageDigest md = MessageDigest.getInstance("SHA");
           md.update(signature.toByteArray());
           Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
           }
   } catch (NameNotFoundException e) {
   } catch (NoSuchAlgorithmException e) {
   }
