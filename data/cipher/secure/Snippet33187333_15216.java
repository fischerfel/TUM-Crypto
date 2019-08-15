//Decrypt cipher
Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
