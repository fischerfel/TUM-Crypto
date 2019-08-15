Cipher c = Cipher.getInstance("AES");
c.init(Cipher.ENCRYPT_MODE, key);
byte[] encValue = c.doFinal(valueToEnc.getBytes());
String encryptedValue = new BASE64Encoder().encode(encValue);
