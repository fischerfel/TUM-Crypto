public static String StringHashing(String s) {

    MessageDigest m = null;
    try {
        m = MessageDigest.getInstance("MD5");

    } catch (NoSuchAlgorithmException ex) {

    }
    m.update(s.getBytes(), 0, s.length());
    return (new BigInteger(1, m.digest()).toString(16));

}
