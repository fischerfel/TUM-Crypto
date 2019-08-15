public String validateAppSignature(Context context) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {

        String currentSignature = "";
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : packageInfo.signatures) {
            byte[] signatureBytes = signature.toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

            Log.d("REMOVED_LOG", "\n" + currentSignature);
        }

        return currentSignature;
    }
