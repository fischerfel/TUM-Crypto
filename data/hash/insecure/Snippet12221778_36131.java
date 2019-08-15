String MD5(String... strings) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
        for(final String s : strings) {
            md.update(s.getBytes());
        }
    } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException("MD5 Cryptography Not Supported");
    }
    final BigInteger bigInt = new BigInteger(1, md.digest());
    return String.format("%032x", bigInt);
}
