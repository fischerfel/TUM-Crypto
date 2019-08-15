private static String privateDecrypt(String text, Key priKey)throws Exception     {
    BASE64Decoder base64Decoder = new BASE64Decoder();
byte[] encryptText = base64Decoder.decodeBuffer(text);
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, priKey);
String decryptedString = new String(cipher.doFinal(encryptText));
return decryptedString;
}
