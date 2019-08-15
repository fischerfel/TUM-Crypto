public static BigInteger hash(String str) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA1");
    digest.reset();
    byte[] input = digest.digest(str.getBytes());

    return new BigInteger(1,input);
}
