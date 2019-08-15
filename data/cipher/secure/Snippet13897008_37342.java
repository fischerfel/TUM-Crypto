Key privateKey = keyStore.getKey("youralias", "password".toCharArray());
PublicKey publicKey = keyStore.getCertificate("youralias").getPublicKey();

KeyGenerator keyGen = KeyGenerator.getInstance("AES");
Key secKey = keyGen.generateKey();

Cipher keyCipher = Cipher.getInstance("RSA");
keyCipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] encryptedKey = keyCipher.doFinal(secKey.getEncoded());

// Write & Read to/from file!

Cipher decryptCipher = Cipher.getInstance("RSA");
decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
byte[] decryptedKey = decryptCipher.doFinal(encryptedKey);

boolean equals = Arrays.equals(secKey.getEncoded(), new SecretKeySpec(decryptedKey, "AES").getEncoded());
System.out.println(equals?"Successfull!":"Failed!");
