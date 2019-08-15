try {
          PackageInfo info = getPackageManager().getPackageInfo(
          "com.fb.project",  //Replace your package name here
          PackageManager.GET_SIGNATURES);

          for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("key hash = "+ Base64.encodeToString(md.digest(),                      Base64.DEFAULT));
          }
    } catch (NameNotFoundException e) {
        e.printStackTrace();

  } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
  }


  got hash key : 2jmj7l5rSw0yVb/vlWAYkK/YBwk=
