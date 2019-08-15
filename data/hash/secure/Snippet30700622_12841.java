try {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(key.getBytes("UTF-8"));
    BigInteger HashValue = new BigInteger(javax.xml.bind.DatatypeConverter.printHexBinary(hash).toLowerCase(), 16);
    String HashValueString = HashValue.toString();
    // ... The rest of your code goes here ....

} catch (NoSuchAlgorithmException e) {
    throw new AssertionError(e);
} catch (UnsupportedEncodingException e) {
    throw new AssertionError(e);
}
