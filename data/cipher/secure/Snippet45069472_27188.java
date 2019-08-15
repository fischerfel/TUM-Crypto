public DaoEncryptionResult<byte[]> getEncryptionResult(final ByteBuffer bufferToEncrypt) {
    try {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        final SecretKey secretKey = new SecretKeySpec(BinaryKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, mSecureRandom);
        final byte[] bytesToEncrypt = bufferToEncrypt.array();
        final byte[] cipherText = cipher.doFinal(bytesToEncrypt,
                bufferToEncrypt.arrayOffset(), bufferToEncrypt.limit());
        final byte[] iv = cipher.getIV();
        return new DaoEncryptionResult<>(cipherText, iv);
    } catch (final GeneralSecurityException securityException) {
        throw new RuntimeException("Could not encrypt data", securityException);
    }
}

public byte[] getDecryptionResult(final byte[] encodedData, final byte[] encodedIv) {
    try {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        final SecretKey secretKey = new SecretKeySpec(BinaryKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encodedIv));
        return cipher.doFinal(encodedData);
    } catch (final GeneralSecurityException securityException) {
        throw new RuntimeException("Could not decrypt data", securityException);
    }
}
