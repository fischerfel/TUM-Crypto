1-try
    {
        PackageInfo info = getPackageManager().getPackageInfo("com.Rapp.app", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures)
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }
    catch (PackageManager.NameNotFoundException e)
    {

    }
    catch (NoSuchAlgorithmException e)
    {

    }


 2-
       keytool -exportcert -alias androiddebugkey -keystore              
      ~/.android/signedkey.keystore > key.out
       cat key.out | openssl sha1 -binary > key.bin
       cat key.bin | openssl base64
