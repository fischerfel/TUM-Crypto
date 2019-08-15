private static final String KEY_TRANSFORMATION_ALGORITHM_SYM = "AES/CBC/NoPadding";

@NonNull
static String encryptMessage(@NonNull String plainMessage,
                             @NonNull SharedPreferences storage,
                             @Nullable Key aesKey,
                             @NonNull String charset) {
    if (aesKey == null) {
        throw new RuntimeException("AES key is null", null);
    }
    try {
        // Cipher can not be re-used on Android
        Cipher cipher = Cipher.getInstance(KEY_TRANSFORMATION_ALGORITHM_SYM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(getIV(storage, cipher, charset)));
        byte[] charsetEncryptedData = cipher.doFinal(plainMessage.getBytes(charset));
        return Base64.encodeToString(charsetEncryptedData, Base64.NO_WRAP);

    } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}

@NonNull
static String decryptMessage(@NonNull String encryptedMessage,
                             @NonNull SharedPreferences storage,
                             @Nullable Key aesKey,
                             @NonNull String charset) {
    if (aesKey == null) {
        throw new RuntimeException("AES key is null", null);
    }
    try {
        //Cipher can not be re-used on Android
        Cipher cipher = Cipher.getInstance(KEY_TRANSFORMATION_ALGORITHM_SYM);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(getIV(storage, cipher, charset)));

        byte[] decryptedData = Base64.decode(encryptedMessage.getBytes(charset), Base64.NO_WRAP);
        byte[] charsetEncryptedData = cipher.doFinal(decryptedData);
        return new String(charsetEncryptedData, charset);

    } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}
