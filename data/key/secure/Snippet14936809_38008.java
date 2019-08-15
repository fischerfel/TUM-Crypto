public static String encrypt(String toEncrypt, byte[ ] key) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[ ] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());
    String encrypted = Base64.encodeBytes(encryptedBytes);
    return encrypted;
}

public static String decrypt(String encryptedText, byte[ ] key) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] toDecrypt = Base64.decode(encryptedText);
    byte[] encrypted = cipher.doFinal(toDecrypt);
    return new String(encrypted);
}
