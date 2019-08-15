byte[] key = //
byte[] encryptedData = //

Cipher c = Cipher.getInstance("AES");
SecretKeySpec k =
  new SecretKeySpec(key, "AES");
c.init(Cipher.DECRYPT_MODE, k);
byte[] data = c.doFinal(encryptedData);
