Key key = new SecretKeySpec(secret.getBytes(), ALGORITHM);

// Encrypt
Cipher cipher = Cipher.getInstance(ALGORITHM);
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encryptedData = cipher.doFinal(plainText);

// Decrypt
cipher.init(Cipher.DECRYPT_MODE, key)
byte[] decryptedData = cipher.doFinal(encryptedData);
