private static final String IV = "nVj1Vakka8jaVn9d";

private static final String AES_MODE = "AES/CBC/PKCS7Padding";
private static final String CHARSET = "UTF-8";

public static String encrypt(String message, String password) {
    try {
        byte[] messageBytes = message.getBytes(CHARSET);
        SecretKeySpec key = new SecretKeySpec(password.getBytes(CHARSET), AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(CHARSET));

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] cipherText = cipher.doFinal(messageBytes);

        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    } catch(Exception ex) {
        return null;
    }
}

public static String decrypt(String base64EncodedCipherText, String password) {
    try {
        byte[] encodedBytes = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);
        SecretKeySpec key = new SecretKeySpec(password.getBytes(CHARSET), AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(CHARSET));

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encodedBytes);

        return new String(decryptedBytes, CHARSET);
    } catch(Exception ex) {
        return null;
    }
}
