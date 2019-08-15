 try {

           PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

           for (Signature signature : info.signatures)
           {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
           }

          } catch (NameNotFoundException e) {
           Log.e("name not found", e.toString());
          } catch (NoSuchAlgorithmException e) {
           Log.e("no such an algorithm", e.toString());
          }
