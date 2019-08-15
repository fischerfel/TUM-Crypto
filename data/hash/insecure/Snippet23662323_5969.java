 // Put this code on oncreate method

try {
    //Replace your.package.name with your base packege name.
    PackageInfo appPkgInfo = getPackageManager().getPackageInfo("your.package.name", PackageManager.GET_SIGNATURES);


    for (Signature signature : appPkgInfo.signatures) {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

        // See Logcat for your hash key and put in on Facebook developer console.
        Log.d("Your Hash key", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
    }
    } catch (NameNotFoundException e) {

    Log.e("ERROR",e.getMessage());

    } catch (NoSuchAlgorithmException e) {

    Log.e("ERROR",e.getMessage());

}
