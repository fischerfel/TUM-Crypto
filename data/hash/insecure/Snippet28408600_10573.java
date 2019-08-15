PackageInfo info;
try {

    info = getPackageManager().getPackageInfo(
        "com.your.package.name", PackageManager.GET_SIGNATURES);

    for (Signature signature : info.signatures) {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        String hash_key = new String(Base64.encode(md.digest(), 0));
    }

} catch (NameNotFoundException e1) {
} catch (NoSuchAlgorithmException e) {
} catch (Exception e) {
}
