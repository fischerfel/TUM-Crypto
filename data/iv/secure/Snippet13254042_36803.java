IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
cipher.init(Cipher.DECRYPT_MODE,keySpec, ivSpec);
