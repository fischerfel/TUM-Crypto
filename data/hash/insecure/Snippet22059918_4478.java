private void printKeyHashForThisDevice() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo("com.package", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());

            String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            Logger.logger("============================================");
            Logger.logger("KeyHash================  ", keyHash);
            Logger.logger("============================================");
            System.out.println("KeyHash================  " + keyHash);

        }
    } catch (NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
}
