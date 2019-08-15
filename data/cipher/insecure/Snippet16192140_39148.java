Cipher aes = Cipher.getInstance("AES");
aes.init(Cipher.DECRYPT_MODE, generateKey());
byte[] ciphertext = aes.doFinal(rawPassword.getBytes());
