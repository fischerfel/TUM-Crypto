public static String getAppSignature(Context context) {
    try {
        for (Signature signature : context.getPackageManager().getPackageInfo(context.getPackageName(),
                PackageManager.GET_SIGNATURES).signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        }
    } catch (Exception e) { /* Do nothing */ }
    return null;
}
