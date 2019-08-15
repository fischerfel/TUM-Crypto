cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));

cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
