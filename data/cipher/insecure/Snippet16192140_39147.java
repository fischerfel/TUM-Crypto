Cipher aes = Cipher.getInstance("AES");
aes.init(Cipher.ENCRYPT_MODE, generateKey());
byte[] ciphertext = aes.doFinal(rawPassword.getBytes());
