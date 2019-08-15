public void generateFBKeyHash(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    "YOUR PACKAGE NAME IN YOUR MANIFEST",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("fb key hash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.e("failed", e.getMessage());
        }
    }
