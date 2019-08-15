public static String hash(String text, String algorithm)
        throws NoSuchAlgorithmException {
    byte[] hash = MessageDigest.getInstance(algorithm).digest(text.getBytes());
    BigInteger bi = new BigInteger(1, hash);
    String result = bi.toString(16);
    if (result.length() % 2 != 0) {
        return "0" + result;
    }
    return result;
}
