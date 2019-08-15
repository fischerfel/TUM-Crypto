public static String decrypt(String ciphertext) {
    key = generateKey(SALT, ENCRYPTION_KEY);
    byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, IV, base64(ciphertext));
    return new String(decrypted, "UTF-8");
}

private static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
    cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
    return cipher.doFinal(bytes);
}

private static SecretKey generateKey(String salt, String passphrase) {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
     KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), ITERATION_COUNT, KEY_SIZE);
     SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
     return key;
 }
