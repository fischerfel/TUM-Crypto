SecretKeySpec key = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key);
cipherIn = new CipherInputStream(new FileInputStream(<decrypted-file>, cipher);
