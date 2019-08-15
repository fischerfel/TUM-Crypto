byte[] key = "1234567891123456".getBytes();
Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");

SecretKeySpec k =  new SecretKeySpec(key, "AES");
c.init(Cipher.ENCRYPT_MODE, k);
byte[] encryptedData = c.doFinal("message".getBytes());
