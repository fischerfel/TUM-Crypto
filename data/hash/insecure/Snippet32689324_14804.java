You can also run the following code:
try {
         PackageInfo info = getPackageManager().getPackageInfo(
                           "yourpackagename", 
                            PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                  }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

          }enter code here
