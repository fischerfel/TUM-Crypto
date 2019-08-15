Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey_AES);
byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
String encryptedMessage = Base64.encodeToString(encrypted, Base64.DEFAULT);
return encryptedMessage;
