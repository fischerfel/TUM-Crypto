Cipher cipherAes = Cipher.getInstance("AES");
cipherAes.init(Cipher.DECRYPT_MODE, secretKeySpec);
byte[] decryptedBytes = cipherAes.doFinal(challengeEncrypted);
