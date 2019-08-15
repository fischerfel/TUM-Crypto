  // Get the private key
  PrivateKey privateKey = (PrivateKey) keyStore.getKey(selectedAlias, "password".toCharArray());
  System.out.println("Key information " + privateKey.getAlgorithm() + " " + privateKey.getFormat());

  // Load aesSessionKey and encryptedMessage
  byte[] aesSessionKey = ...
  byte[] encryptedMessage = ...

  // RSA Decryption of Encrypted Symmetric AES key - 128 bits
  Cipher rsaCipher = Cipher.getInstance("RSA", "BC");
  rsaCipher.init(Cipher.UNWRAP_MODE, privateKey);
  Key decryptedKey = rsaCipher.unwrap(aesSessionKey, "AES", Cipher.SECRET_KEY);
  System.out.println("Decrypted Key Length: " + decryptedKey.getEncoded().length);

  SecretKeySpec decrypskeySpec = new SecretKeySpec(decryptedKey.getEncoded(), "AES");
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "BC");
  cipher.init(Cipher.DECRYPT_MODE, decryptedKey, new IvParameterSpec(new byte[16]));
  byte[] message = cipher.doFinal(encryptedMessage);
  System.out.println(new String(message, "UTF-8"));
