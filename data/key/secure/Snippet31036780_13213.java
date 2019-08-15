Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
byte[] cipherText = cipher.doFinal(plaintext);
byte[] iv = cipher.getIV();   //The problematic IV
