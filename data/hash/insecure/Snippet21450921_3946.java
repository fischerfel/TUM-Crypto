private static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    crypt.reset();
    crypt.update(password.getBytes("UTF-8"));

    return new BigInteger(1, crypt.digest()).toString(16);
}
