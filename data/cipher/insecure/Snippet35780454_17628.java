Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
byte[] ciphertext = c.doFinal(plaintext);
