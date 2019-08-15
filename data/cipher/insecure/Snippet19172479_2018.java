//Encryption 
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
 String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
return encryptedString;

//Decryption
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
cipher.init(Cipher.DECRYPT_MODE, secretKey);
String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
return decryptedString;
