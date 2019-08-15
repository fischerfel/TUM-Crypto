public static String Decrypt(String encryptedText, byte[] key2) throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    byte[] decryptedTextBytes=null;
    byte[] key3 =null;
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key3= sha.digest(key2);
    key3 = copyOf(key3, 16);
    SecretKeySpec keySpec = new SecretKeySpec(key3, "AES");
    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    byte[] encryptedTextBytes = new Base64().decode(encryptedText);
    decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    return new String(decryptedTextBytes);
}
