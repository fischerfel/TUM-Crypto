private String getAppKeyHash() {
  try {
    PackageInfo info = getPackageManager().getPackageInfo(
            getPackageName(), PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md;

        md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        String something = new String(Base64.encode(md.digest(), 0));
        return something;

    }
} catch (Exception e) {
    Log.e("exception", e.toString());
}
return null;
