//Decrypt cipher
Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
