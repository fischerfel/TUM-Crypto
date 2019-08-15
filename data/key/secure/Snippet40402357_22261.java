public static String encrypt(String plainText) {
    try {
        byte[] keyData = secret_key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(keyData, "AES/ECB/PKCS7Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptedString = Base64.encodeToString(cipherText, Base64.NO_WRAP);

        return encryptedString;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
