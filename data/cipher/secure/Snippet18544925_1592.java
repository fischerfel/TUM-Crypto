public static String md5(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest(input.getBytes());
    BigInteger number = new BigInteger(1, messageDigest);
    return number.toString(16);
}

public String decrypt(String encryptedData, String initialVectorString, String secretKey) {
    String decryptedData = null;
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(md5(secretKey).getBytes(), "AES");
        IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);
        byte[] encryptedByteArray = (new org.apache.commons.codec.binary.Base64()).decode(encryptedData.getBytes());
        byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
        decryptedData = new String(decryptedByteArray, "UTF8");
    } catch (Exception e) {
        LOGGER.debug("Problem decrypting the data", e);
    }
    return decryptedData;
}
