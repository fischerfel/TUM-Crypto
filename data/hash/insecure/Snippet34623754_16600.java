private static final int VALID = 0;
private static final int INVALID = 1;

public static int checkAppSignature(Context context) {

    try {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET\_SIGNATURES);

        for (Signature signature : packageInfo.signatures) {
            byte[] signatureBytes = signature.toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

            //compare signatures
            if (SIGNATURE.equals(currentSignature)){
                return VALID;
            };
        }
    } catch (Exception e) {
        //assumes an issue in checking signature., but we let the caller decide on what to do.
    }

    return INVALID;
}
