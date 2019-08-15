    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sec, "AES"),new IvParameterSpec(new byte[cipher.getBlockSize()]));
    byte[] encode = cipher.doFinal(data);

    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sec, "AES"), new IvParameterSpec(new byte[cipher.getBlockSize()]));
    byte[] decode = cipher.doFinal(encode);
