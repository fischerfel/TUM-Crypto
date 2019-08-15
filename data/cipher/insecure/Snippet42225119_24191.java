private static byte[] encryptText(String plaintext, byte[] keyBytes) throws Exception {
    // Get plaintext as ASCII byte array
    final byte[] plainBytes;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        plainBytes = plaintext.getBytes(StandardCharsets.US_ASCII);
    } else {
        plainBytes = plaintext.getBytes("US-ASCII");
    }

    // Generate triple DES key from byte array
    final DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
    SecretKey key = keyFactory.generateSecret(keySpec);

    // Setup the cipher
    final Cipher c3des = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    c3des.init(Cipher.ENCRYPT_MODE, key);

    // Return ciphertext
    return c3des.doFinal(plainBytes);
}
