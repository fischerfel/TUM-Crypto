    public static String md5EncryptString(String string, String salt) {
    MessageDigest messageDigest;
    String encryptString = string + salt;
    String result;
    try {
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(encryptString.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        result = new String(Hex.encodeHex(resultByte));
        return result;
    } catch (NoSuchAlgorithmException e) {
        logger.error("NoSuchAlgorithmException in encryptString");
        result = encryptString;
    }
    return result;
}
