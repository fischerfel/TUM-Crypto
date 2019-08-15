cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptCipher.getIV()));
