Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decryptedBytes = cipher.doFinal(encryptedBytes, 0, 128);
