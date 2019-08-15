public static String encrypt(String data, Key key) throws Exception {

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes());
    byte[] base64Bytes = Base64.encodeBase64(encryptedBytes);
    String base64EncodedString = new String(base64Bytes);
    return base64EncodedString;
}

public static String decrypt(String encrypted, Key key) throws Exception {

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decoded = Base64.decodeBase64(encrypted.getBytes());
    byte[] decrypted = cipher.doFinal(decoded);
    return new String(decrypted);
}
