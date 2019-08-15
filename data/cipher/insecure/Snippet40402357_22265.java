public static String decrypt(String encryptedText) {
    try {
        byte[] keyData = secret_key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(keyData, "AES/ECB/PKCS7Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] cipherText = Base64.decode(encryptedText,Base64.NO_WRAP);
        String decryptedString = new String(cipher.doFinal(cipherText),"UTF-8");

        return decryptedString;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
