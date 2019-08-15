  public AESEncrypter(SecretKey key) {
    // Create an 8-byte initialization vector
    byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
            0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    try {
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // CBC requires an initialization vector
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
