    // Add code to print out the key hash
    try {
        String PACKAGE_NAME=getApplicationContext().getPackageName();;
        PackageInfo info = getPackageManager().getPackageInfo(
                PACKAGE_NAME, 
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }       
