Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");//Gave padding during encryption too
aes.init(Cipher.ENCRYPT_MODE, generateKey());
byte[] ciphertext = aes.doFinal(rawPassword.getBytes());
