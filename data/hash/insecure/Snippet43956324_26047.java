 try {
        PackageInfo info =     getPackageManager().getPackageInfo("MY PACKAGE NAME",     PackageManager.GET_SIGNATURES);
        for (android.content.pm.Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.e("MY KEY HASH:", sign);

        }
} catch (NameNotFoundException e) {
} catch (NoSuchAlgorithmException e) {
}
