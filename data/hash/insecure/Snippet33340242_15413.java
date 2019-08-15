try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "my.package.name",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("KeyHash:", "++++++++++++++++++++++++++++++++++++++" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.e("KeyHash:", "++++++++++++++++++++++++++++++++++++++" + e.toString());

    } catch (NoSuchAlgorithmException e) {
        Log.e("KeyHash:", "++++++++++++++++++++++++++++++++++++++" + e.toString());
    }
