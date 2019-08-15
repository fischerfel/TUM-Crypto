private String hashMD5(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("MD5");

    // BYTES_ENCODING == "UTF-8"
    digest.update(input.getBytes(BYTES_ENCODING), 0, input.length());

    System.out.println(new BigInteger(1, digest.digest()).toString(16));
    System.out.println(new BigInteger(1, digest.digest()).toString(16));
    System.out.println(new BigInteger(1, digest.digest()).toString(16));

    String hash = new BigInteger(1, digest.digest()).toString(16);
    System.out.println(hash + " : used hash");
    System.out.println(hash.length() + " - " + new BigInteger(1, digest.digest()).toString(16).length());
    return hash;
}
