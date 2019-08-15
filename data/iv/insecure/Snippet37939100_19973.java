private static final byte[] IV = new byte[16];
...
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));
