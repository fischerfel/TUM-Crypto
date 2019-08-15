public String decrypt(String cipherText)throws Exception {
   final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
   final String ALGORITHM="RSA";
   final String CHARCTER_ENCODING = "UTF-8";
   PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new KCS8EncodedKeySpec(IOUtils.toByteArray(new      FileInputStream(PRIVATE_KEY_FILE_PATH)));
   Cipher cipher = Cipher.getInstance(TRANSFORMATION);   cipher.init(Cipher.DECRYPT_MODE,KeyFactory.getInstance(ALGORITHM).generatePrivate(pkcs8EncodedKeySpec));
   return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), CHARCTER_ENCODING);
}
