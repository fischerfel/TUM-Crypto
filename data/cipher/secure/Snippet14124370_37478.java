Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
AESCipher.init(Cipher.DECRYPT_MODE, secretKey, secRandom);
