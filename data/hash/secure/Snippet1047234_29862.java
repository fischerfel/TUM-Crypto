public static String hash(String text, String algorithm)
        throws NoSuchAlgorithmException {
    byte[] hash = MessageDigest.getInstance(algorithm).digest(text.getBytes());
    return new BigInteger(1, hash).toString(16);
}
