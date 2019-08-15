    public static String decryption(byte[] encryptedIvTextBytes, String key) throws Exception {

    int ivSize = 16;
    int keySize = 16;

    // Extract IV.
   byte[] iv = new byte[ivSize];
    System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);

    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    // Extract encrypted part.
   int encryptedSize = encryptedIvTextBytes.length - ivSize;
    byte[] encryptedBytes = new byte[encryptedSize];
    System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

    // Hash key.
    byte[] keyBytes = new byte[keySize];
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(key.getBytes());
    System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

    // Decrypt.
Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

    return new String(decrypted);

}  
