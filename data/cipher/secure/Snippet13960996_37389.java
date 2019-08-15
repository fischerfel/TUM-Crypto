Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, publicKey);
byte[] plainBytes = cipher.doFinal(cipherBytes);
