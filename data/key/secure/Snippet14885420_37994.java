SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
cryptoTool.init(Cipher.ENCRYPT_MODE, key); // This is where the error fires.

return String.valueOf(cryptoTool.doFinal(plaintext.getBytes()));
