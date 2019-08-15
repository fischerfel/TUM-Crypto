Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding"); //I am passing the padding too
aes.init(Cipher.DECRYPT_MODE, generateKey());
byte[] ciphertext = aes.doFinal(rawPassword.getBytes());
