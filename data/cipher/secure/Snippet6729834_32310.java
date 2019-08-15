 byte[] sessionKey = {00000000000000000000000000000000};
 byte[] iv = {00000000000000000000000000000000};
 byte[] plaintext = "6a84867cd77e12ad07ea1be895c53fa3".getBytes();
 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

 cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
 byte[] ciphertext = cipher.doFinal(plaintext);

 cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
 byte[] deciphertext = cipher.doFinal(ciphertext);
