 Cipher cipher = Cipher.getInstance("RSA");
 cipher.init(Cipher.ENCRYPT_MODE, publicKey);
 byte[] cipherData = cipher.doFinal(content);
