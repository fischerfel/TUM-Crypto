private static String publicEncrypt(String text, Key pubKey) throws Exception {
    BASE64Encoder bASE64Encoder = new BASE64Encoder();
    byte[] plainText = text.getBytes();
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
String encryptedText = bASE64Encoder.encode(cipher.doFinal(plainText));
return encryptedText;
}
