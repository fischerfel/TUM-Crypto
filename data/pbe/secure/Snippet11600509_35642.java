    SecureRandom saltRand = new SecureRandom(new byte[] { 1, 2, 3, 4 });
    byte[] salt = new byte[16];
    saltRand.nextBytes(salt);

    int keyLength = 3248;
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 8192, keyLength);
    SecretKey key = factory.generateSecret(spec);

    SecureRandom keyGenRand = SecureRandom.getInstance("SHA1PRNG");
    keyGenRand.setSeed(key.getEncoded());

    KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
    gen.initialize(keyLength, keyGenRand);
    java.security.KeyPair p = gen.generateKeyPair();
