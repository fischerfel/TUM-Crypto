 byte[] iv = generateIv(cipher.getBlockSize());
 IvParameterSpec ivParams = new IvParameterSpec(iv);
 cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
