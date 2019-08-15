   try {
        PackageInfo info = getPackageManager().getPackageInfo(
            "COM.YOUR.PACKAGE.NAME", 
            PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("My Keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (Exception e) {
        Log.e("My Keyhash", e.toString());
    } 
