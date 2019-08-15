byte iv[] = encryptCipher.getIV(); 
IvParameterSpec dps = new IvParameterSpec(iv);
decryptCipher.init(Cipher.DECRYPT_MODE, key, dps);
