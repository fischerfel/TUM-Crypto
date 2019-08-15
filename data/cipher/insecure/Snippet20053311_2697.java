private static String seed;

/**
 * Encrypts the text. 
 * @param clearText The text you want to encrypt
 * @return Encrypted data if successful, or null if unsucessful
 */
protected String encrypt(String clearText) {
    byte[] encryptedText = null;
    try {
        byte[] keyData = seed.getBytes();
        SecretKey ks = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, ks);
        encryptedText = c.doFinal(clearText.getBytes("UTF-8"));
        return Base64.encodeToString(encryptedText, Base64.DEFAULT);
    } catch (Exception e) {
        return null;
    }
}

/**
 * Decrypts the text
 * @param encryptedText The text you want to encrypt
 * @return Decrypted data if successful, or null if unsucessful
 */
protected String decrypt (String encryptedText) {
    byte[] clearText = null;
    try {
        byte[] keyData = seed.getBytes();
        SecretKey ks = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, ks);
        clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
        return new String(clearText, "UTF-8");
    } catch (Exception e) {
        return null;
    }
}   
