byte[] cipherText;
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, pk);
cipherText = cipher.doFinal("Hello World!".getBytes());
return Base64.encodeBase64String(cipherText);
