public Cipher getDecryptCipher(String deviceId) {
    try {
        String encodedIv = mSharedPreferences.getString(createIvKey(deviceId), "");
        byte[] cipherIv = Base64.decode(encodedIv, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        mKeyStore.load(null);
        SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(cipherIv));
        return cipher;
    } catch (Exception e) {
        throw new RuntimeException("Failed to encrypt pin ", e);
    }
}
