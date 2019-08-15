    Cipher aes = Cipher.getInstance("AES/CBC/PKCS7Padding");

    SecretKey key = new SecretKeySpec(keyBytes, "AES");

    // Initialize the cipher
    aes.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));

    guid = new String(aes.doFinal(challengeBytes));

    return guid;
