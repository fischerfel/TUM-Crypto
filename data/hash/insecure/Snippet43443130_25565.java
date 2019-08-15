private String getSHA1(String packageName){
    try {
        Signature[] signatures = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
        for (Signature signature: signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(signature.toByteArray());
            return BaseEncoding.base16().encode(md.digest());
        }
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return null;
}
