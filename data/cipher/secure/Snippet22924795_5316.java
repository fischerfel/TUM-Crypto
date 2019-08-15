public static String encrypt(String data, RSAPublicKey key) throws Exception {

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    return Base64.encodeBase64String(encryptedBytes);
}

public static String decrypt(String encrypted, RSAPrivateKey key) throws Exception {

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decoded = Base64.decodeBase64(encrypted);
    byte[] decrypted = cipher.doFinal(decoded);
    return new String(decrypted, StandardCharsets.UTF_8);
}
