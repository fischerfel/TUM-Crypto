public void printHashKey() {
   try {
      PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.gorbin.androidsocialnetworksextended.asne",
      PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());
         Log.d("HASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
   } catch (PackageManager.NameNotFoundException e) {
   } catch (NoSuchAlgorithmException e) {
   }
   }
