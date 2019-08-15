// Add code to print out the key hash
PackageInfo info = getPackageManager().getPackageInfo("com.my.package", PackageManager.GET_SIGNATURES);
for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());

        Log.d("KeyHash1:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        Log.d("KeyHash2:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

    }
