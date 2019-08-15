public static String checkAppSignature(Context context) {
    try {
        PackageInfo packageInfo = context.getPackageManager()
                .getPackageInfo(context.getPackageName(),
                        PackageManager.GET_SIGNATURES);
        for (Signature signature : packageInfo.signatures) {
            byte[] signatureBytes = signature.toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signatureBytes);
            final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            //signature at runtime
            return currentSignature;
        }
    } catch (Exception e) {
        //something went wrong, let caller decide on what to do.
    }
    return "INVALID_SIGNATURE";
}
