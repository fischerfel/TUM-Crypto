byte[] key = //... secret sequence of bytes
byte[] dataToSend = ...
Cipher c = Cipher.getInstance("AES");
SecretKeySpec k = new SecretKeySpec(key, "AES");
c.init(Cipher.ENCRYPT_MODE, k);
byte[] encryptedData = c.doFinal(dataToSend);
