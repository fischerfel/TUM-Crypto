PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures)
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(signature.toByteArray());
            Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
