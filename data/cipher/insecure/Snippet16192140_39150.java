Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
aes.init(Cipher.DECRYPT_MODE, generateKey());
byte[] ciphertext = aes.doFinal(rawPassword.getBytes());
