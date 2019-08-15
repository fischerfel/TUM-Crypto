// Add code to print out the key hash
PackageInfo info = getPackageManager().getPackageInfo("com.your.package", // replace with your package name 
PackageManager.GET_SIGNATURES);
for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());

        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

    }
