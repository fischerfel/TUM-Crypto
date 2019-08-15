    /* Store these things on disk used to derive key later: */
    int iterationCount = 1000;
    int saltLength = 32; // bytes; should be the same size as the output
                            // (256 / 8 = 32)
    int keyLength = 256; // 256-bits for AES-256, 128-bits for AES-128, etc
    byte[] salt = new byte[saltLength]; // Should be of saltLength

    /* When first creating the key, obtain a salt with this: */
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);

    /* Use this to derive the key from the password: */
    KeySpec keySpec = new PBEKeySpec(new String(key,
            Constants.CHAR_ENCODING).toCharArray(), key, iterationCount,
            keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

    return secretKey.getEncoded();
