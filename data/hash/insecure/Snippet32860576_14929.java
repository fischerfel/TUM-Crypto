public void traceKeyHash(Activity activity){
    try {
        PackageInfo info = activity.getPackageManager().getPackageInfo("your.package.here", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i(getClass().getName(), "Share - KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
