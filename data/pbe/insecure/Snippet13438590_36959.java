    /* User types in their password: */
    String password = "password";

    /* Store these things on disk used to derive key later: */
    int iterationCount = 1000;
    int saltLength = 32; // bytes; should be the same size as the output (256 / 8 = 32)
    int keyLength = 256; // 256-bits for AES-256, 128-bits for AES-128, etc
    byte[] salt; // Should be of saltLength

    /* When first creating the key, obtain a salt with this: */
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[saltLength];
    random.nextBytes(salt);

    /* Use this to derive the key from the password: */
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
    byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    SecretKey key = new SecretKeySpec(keyBytes, "AES");
