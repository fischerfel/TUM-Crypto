import android.content.pm.Signature;

      try {
        PackageInfo info = getPackageManager().getPackageInfo(
                **"do not forgot to your package name"**, PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }



OR 

1. for Android default keystore  : add this to in your terminal 
 
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64

2. for signed keystore 

keytool -exportcert -alias aliasname -keystore keystorename | openssl sha1 -binary | openssl base64
