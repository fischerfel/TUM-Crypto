private String decrypt(String initial) throws Exception {

    SecretKeySpec skeySpec = new SecretKeySpec(md5("the key").getBytes("UTF-8"), "AES");
    IvParameterSpec initialVector = new IvParameterSpec("the vector".getBytes("UTF-8"));
    Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);
    byte[] encryptedByteArray = (new org.apache.commons.codec.binary.Base64()).decode(initial.getBytes("UTF-8"));
    byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);

    return (new String(decryptedByteArray, "UTF8"));
}

private String md5(String input) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
    BigInteger number = new BigInteger(1, messageDigest);

    return number.toString(16);
}
