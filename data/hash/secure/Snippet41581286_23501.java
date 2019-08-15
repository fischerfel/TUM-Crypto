public static String Encode_SHA512(String input) {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    byte[] digest = md.digest(input.getBytes("UTF-16LE"));
    return String.format("%0128x", new BigInteger(1, digest));
}
