public static String encryptText(String textToEncrypt) {
    try {
        byte[] guid = "1234567890123456".getBytes("UTF-8");

        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ips = new IvParameterSpec(iv);

        // The secret key from the server needs to be converted to byte array for encryption.
        byte[] secret = ENCRYPTION_SECRET_HASH.getBytes("UTF-8");

        // we generate a AES SecretKeySpec object which contains the secret key.
        // SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, ips);

        byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes());
        byte[] base64encodedSecretData = Base64.encodeBase64(cipherText);
        String secretString = new String(base64encodedSecretData);
        return secretString;
    } catch (Exception e) {
        e.printStackTrace();
        Log.e(TAG, "Encryption error for " + textToEncrypt, e);
    }
    return "";
}
