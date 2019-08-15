PackageInfo info;
try {
    info = getContext().getPackageManager().getPackageInfo("com.mypackage", PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        return new String(Base64.encode(md.digest(), 0));
    }
} catch (PackageManager.NameNotFoundException e) {
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (Exception e) {
    e.printStackTrace();
}
