private static final String SIGNATURE = "TLFjqb41Fg+W/qAEw7GMefvr2VY=";

PackageInfo packageInfo = context.getPackageManager()
     .getPackageInfo(context.getPackageName(),
          PackageManager.GET_SIGNATURES);

for (Signature signature : packageInfo.signatures) {
    byte[] signatureBytes = signature.toByteArray();

    MessageDigest md = MessageDigest.getInstance("SHA");

    md.update(signature.toByteArray());

    final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

    Log.d("REMOVE_ME", "Include this string as a value for SIGNATURE " + currentSignature);
    Log.d("REMOVE_ME", "SIGNATURE as a value for SIGNATURE value:" + SIGNATURE);

    //compare signatures

    if (SIGNATURE.equals(currentSignature)){
        Log.d("REMOVE_ME", "in  IF loop");
        return VALID;

    }else{
        Log.d("REMOVE_ME", "in  else loop");
        return INVALID;
    }
