public static String encrypt(String key, String toEncrypt) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
    byte[] encryptedValue = Base64.encodeBase64(encrypted);
    return new String(encryptedValue);
}

public static String decrypt(String key, String encrypted) throws Exception {
    Key skeySpec = generateKeySpec(key);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decodedBytes = Base64.decodeBase64(encrypted.getBytes());
    byte[] original = cipher.doFinal(decodedBytes);
    return new String(original);
}
