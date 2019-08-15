PackageInfo info = getPackageManager().getPackageInfo(
        "Your Pakage name", 
        PackageManager.GET_SIGNATURES);
for (Signature signature : info.signatures) {
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(signature.toByteArray());
    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
