Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encryptedBytes = cipher.doFinal(inputBytes, 0, 128);
