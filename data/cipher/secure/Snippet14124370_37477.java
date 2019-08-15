Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
AESCipher.init(Cipher.ENCRYPT_MODE, secretKey, secRandom);
