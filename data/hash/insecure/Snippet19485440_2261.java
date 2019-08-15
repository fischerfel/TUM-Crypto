try {
        PackageInfo info =     getPackageManager().getPackageInfo("com.package.mypackage",     PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.e("MY KEY HASH:", sign);
            Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
        }
} catch (NameNotFoundException e) {
} catch (NoSuchAlgorithmException e) {
}
