public static String md5(String s) {
    MessageDigest md5;
    try {
        md5 = MessageDigest.getInstance("MD5");
        md5.update(s.getBytes());
        return new BigInteger(1, md5.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    }
}
