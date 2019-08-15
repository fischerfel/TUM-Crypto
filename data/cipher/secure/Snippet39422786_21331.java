SecretKey key = keyFactory.generateSecret(keySpec);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] ciphertext = cipher.doFinal(cleartext);

return bytes2String(ciphertext);
