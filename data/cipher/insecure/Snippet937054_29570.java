
Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
SecretKeySpec key = new SecretKeySpec(yourKeyBytes, "AES");
aes.init(Cipher.DECRYPT_MODE, key);
byte[] cleartext = aes.update(ciphertext, 0, ciphertext.length);
