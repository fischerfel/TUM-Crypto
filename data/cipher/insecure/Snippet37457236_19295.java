    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
    // AES defaults to AES/ECB/PKCS5Padding in Java 7
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
    byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
    return byteCipherText;}


    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
    // AES defaults to AES/ECB/PKCS5Padding in Java 7
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.DECRYPT_MODE, secKey);
    byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
    return new String(bytePlainText);
}
