 final SecretKey key = new SecretKeySpec(keyBytes, "DESede");

 final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

 cipher.init(Cipher.ENCRYPT_MODE, key);

 final byte[] cipherText = cipher.doFinal(plainTextBytes);
