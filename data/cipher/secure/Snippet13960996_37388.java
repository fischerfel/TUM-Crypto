Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
