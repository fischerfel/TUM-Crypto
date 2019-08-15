public static String MD5hash(String text) throws NoSuchAlgorithmException {
    byte[] hash = MessageDigest.getInstance("MD5").digest(text.getBytes());
    return String.format("%032x",new BigInteger(1, hash));
}
