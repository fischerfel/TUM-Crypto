  public static String getSignatureHash(Context ctxt, String packageName)
                                                                         throws NameNotFoundException,
                                                                         NoSuchAlgorithmException {
    MessageDigest md=MessageDigest.getInstance("SHA-1");
    Signature sig=
        ctxt.getPackageManager()
            .getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0];

    return(toHexStringWithColons(md.digest(sig.toByteArray())));
  }
