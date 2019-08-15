Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
aes.init(Cipher.ENCRYPT_MODE, key);
byte[] ciphertext = aes.doFinal("my cleartext".getBytes());
byte[] iv = aes.getIV();
