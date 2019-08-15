    // Decode EncryptKey with GameId
    byte[] gameIdBytes = ("502719605").getBytes();
    SecretKeySpec gameIdKeySpec = new SecretKeySpec(gameIdBytes, "Blowfish");
    Cipher gameIdCipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    gameIdCipher.init(Cipher.DECRYPT_MODE, gameIdKeySpec);
    byte[] encryptKeyBytes = Base64.decode("Sf9c+zGDyyST9DtcHn2zToscfeuN4u3/");
    byte[] encryptkeyDecryptedByGameId = gameIdCipher.doFinal(encryptKeyBytes);

    // Initialize the chunk cipher
    SecretKeySpec chunkSpec = new SecretKeySpec(encryptkeyDecryptedByGameId, "Blowfish");
    Cipher chunkCipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    chunkCipher.init(Cipher.DECRYPT_MODE, chunkSpec);

    byte[] chunkContent = getChunkContent();
    byte[] chunkDecryptedBytes = chunkCipher.doFinal(chunkContent);
