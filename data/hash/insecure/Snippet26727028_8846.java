private void getSHAFingerPrint(){
      PackageInfo info;
      try {

          info = getPackageManager().getPackageInfo(
                  "your.package", PackageManager.GET_SIGNATURES);

          for (Signature signature : info.signatures) {
              MessageDigest md;
              md = MessageDigest.getInstance("SHA");
              md.update(signature.toByteArray());
              String something = new String(Base64.encode(md.digest(), 0));
              Log.e("Hash key", something);
              System.out.println("Hash key" + something);
          }

      } catch (NameNotFoundException e1) {
          Log.e("name not found", e1.toString());
      } catch (NoSuchAlgorithmException e) {
          Log.e("no such an algorithm", e.toString());
      } catch (Exception e) {
          Log.e("exception", e.toString());
      }
  }
