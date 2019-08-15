cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(IV1));
cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(IV2));
