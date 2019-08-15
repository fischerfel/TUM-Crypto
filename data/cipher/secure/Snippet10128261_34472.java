Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
byte[] encryptedText = cipher.doFinal("xxxxx|xxxxx".getBytes("UTF-8"));
String encodedText = new BASE64Encoder().encode(encryptedText);
