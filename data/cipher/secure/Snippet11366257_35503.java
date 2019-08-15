private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance(encryptionAlgorithim);
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iVector));
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}

public String decrypt(String encryptedString, String key) {

    byte[] keyBytes = key.getBytes();
    byte[] decoded = Base64.decode(encryptedString); // Decodes the string from base64 to byte[]
    byte[] result = decrypt(keyBytes, decoded);
    return new String(result);
}
