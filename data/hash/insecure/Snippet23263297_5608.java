    // Create MD5 Hash
    MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
    digest.update(s.getBytes());
    byte[] md5sum = digest.digest();
    BigInteger bigInt = new BigInteger(1, md5sum);
    String stringMD5 = bigInt.toString(16);
    // Fill to 32 chars
    stringMD5 = String.format("%32s", stringMD5).replace(' ', '0');
    return stringMD5;
