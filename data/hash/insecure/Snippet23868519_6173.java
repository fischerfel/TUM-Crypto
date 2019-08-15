try {
         PackageInfo info = getPackageManager().getPackageInfo(
         "my pkg name",
        /// android.test.purchased
         PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());

         Base64.DEFAULT));
         Base64.DEFAULT));
         }
         } catch (NameNotFoundException e) {

         } catch (NoSuchAlgorithmException e) {

         }
