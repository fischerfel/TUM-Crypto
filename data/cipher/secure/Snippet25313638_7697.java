    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(1024);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    SecretKey sessionKey = keyGen.generateKey();

    // Encrypt the session key with the RSA public key
    Cipher rsaCipher = Cipher.getInstance("RSA");
    rsaCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
    byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());

    // Simulating other end user: Receive encrypted session key,
    // decrypt it using your private key.
    Cipher rsaDecryptCipher = Cipher.getInstance("RSA");
    rsaDecryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
    byte[] decryptedSessionKeyBytes = rsaCipher.doFinal(encryptedSessionKey);
    System.out.println("Decrypted session key bytes");
    System.out.println(decryptedSessionKeyBytes);
