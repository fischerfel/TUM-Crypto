 cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes));
 cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes));
