//Convert bytes to AES SecertKey
SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey .length, "AES");
Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
String plainText = new String(bytePlainText);`
