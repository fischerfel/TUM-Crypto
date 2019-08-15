    int iterationCount = 1000;
    int saltLength = 8; // bytes; 64 bits
    int keyLength = 256;
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[saltLength];
    random.nextBytes(salt);
    KeySpec keySpec = new PBEKeySpec(seed.toCharArray(), salt,
            iterationCount, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    byte[] raw = keyFactory.generateSecret(keySpec).getEncoded();
