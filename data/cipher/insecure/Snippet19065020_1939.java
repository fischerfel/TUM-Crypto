Cipher cipher=Cipher.getInstance("AES/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
byte[] encryptedData=cipher.doFinal(plaintext);  // will be 16 bytes
