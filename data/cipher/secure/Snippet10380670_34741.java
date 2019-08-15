Cipher cipherAes = Cipher.getInstance("AES/CBC/PKCS7Padding");
cipherAes.init(Cipher.DECRYPT_MODE, secretKeySpec);
byte[] decryptedBytes = cipherAes.doFinal(encrypted);
String decryptedString = new String(decryptedBytes);
