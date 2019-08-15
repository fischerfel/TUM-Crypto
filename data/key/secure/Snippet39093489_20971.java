Key key = new SecretKeySpec(keyValue, "AES");
Cipher c = Cipher.getInstance("AES");
c.init(1, key);
byte[] encVal = c.doFinal(Data.getBytes());
encryptedValue = new BASE64Encoder().encode(encVal);
