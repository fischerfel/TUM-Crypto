try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.example.facebooktest", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.d("KeyHash:", s);
        }
    } catch (Exception e) {
    }
