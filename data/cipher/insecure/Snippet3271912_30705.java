decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
decryptCipher.init(2, getKey(0));
decryptCipher.doFinal(data);
