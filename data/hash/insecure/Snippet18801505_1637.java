try{ logger.debug("Checking signs");
    PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
    for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        logger.debug(Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }
} catch (NameNotFoundException e) {
    e.printStackTrace();
    logger.debug(e.getMessage());
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
    logger.debug(e.getMessage());
}`
