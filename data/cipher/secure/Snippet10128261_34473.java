Cipher cipherDec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipherDec.init(Cipher.DECRYPT_MODE, key.getPrivate());
byte[] decodedText = new BASE64Decoder().decodeBuffer(encodedText);
byte[] decryptedText = cipherDec.doFinal(decodedText);
String finalValue = new String(decryptedText, "UTF-8");
