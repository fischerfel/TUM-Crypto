String SIGNATURE = "HmdQ7mF9uZ2unNb8qz1HEuD+iT4=";

    try {

        PackageInfo packageInfo = context.getPackageManager()

            .getPackageInfo(context.getPackageName(),

                            PackageManager.GET_SIGNATURES);

        for (Signature signature : packageInfo.signatures) {

            byte[] signatureBytes = signature.toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA");

            md.update(signature.toByteArray());

            final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Log.d("1",currentSignature);
            Log.d("2",SIGNATURE);
            Log.d("equals:",
                  currentSignature.equals(SIGNATURE)?"true":"false");

        }
    } catch (Exception e) {
    }
