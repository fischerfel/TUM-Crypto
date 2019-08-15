public static String buildMD5PasswordDigest(String nonce, String created,
        String password)  {
    String passwordDigest = null;

    try {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        sha1.update(nonce.getBytes("UTF-8"));
        sha1.update(created.getBytes("UTF-8"));

        byte[] digest = sha1
                .digest(password.getBytes("UTF-8"));
        BigInteger bigInt = new BigInteger(1,digest);
        passwordDigest = bigInt.toString(16);
        sha1.reset();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        LogServiceImpl.logError(e);
    }

    return passwordDigest;
}
