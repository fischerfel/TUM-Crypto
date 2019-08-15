try {
        PackageInfo info = getPackageManager().getPackageInfo(
        "com.bloopit.activities", 
        PackageManager.GET_SIGNATURES);
          for (Signature signature : info.signatures) {
           MessageDigest md = MessageDigest.getInstance("SHA");
           md.update(signature.toByteArray());
           System.out.println("KeyHash : "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
