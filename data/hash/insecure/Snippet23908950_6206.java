try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.facebook.samples.hellofacebook",
                PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("KeyHash:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {
        Log.d("NameNotFoundException", "NameNotFoundException");
    } catch (NoSuchAlgorithmException e) {
        Log.d("NameNotFoundException", "NoSuchAlgorithmException");
    }
