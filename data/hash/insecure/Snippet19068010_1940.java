   try {
        PackageInfo info = this.getPackageManager().getPackageInfo("com.mypacket", PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("Hash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (NameNotFoundException e) {
        Log.e("Hash", "Error: NameNotFoundException");
        e.printStackTrace();

    } catch (NoSuchAlgorithmException e) {
        Log.e("Hash", "Error: NoSuchAlgorithmException");
    }
