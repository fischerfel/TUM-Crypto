Cipher decrypt=Cipher.getInstance("RSA");
decrypt.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decryptedMessage=decrypt.doFinal(encryptedMessage);
