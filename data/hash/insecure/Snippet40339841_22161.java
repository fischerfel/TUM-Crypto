try {
         PackageInfo info = getPackageManager().getPackageInfo(
         "com.app.package",
         PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());
         Log.d("KeyHash", "KeyHash:"+ Base64.encodeToString(md.digest(),
         Base64.DEFAULT));
         Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
                 Base64.DEFAULT), Toast.LENGTH_LONG).show();
         }
         } catch (NameNotFoundException e) {

         } catch (NoSuchAlgorithmException e) {

         }
