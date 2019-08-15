 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
 Cipher cipher = Cipher.getInstance("AES");
 cipher.init(Cipher.DECRYPT_MODE, skeySpec);
 decrypted = cipher.doFinal(encryptedData);
