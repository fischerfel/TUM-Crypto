Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, secretKey_AES);
byte[] decrypted = cipher.doFinal(Base64.decode(message,Base64.DEFAULT));
String decryptedMessage = new String(Base64.encode(decrypted, Base64.DEFAULT));
return decryptedMessage;
