    byte[] randomSalt = new byte[8];
    SecureRandom secRand = new SecureRandom();
    secRand.nextBytes(randomSalt);

    String randomPassword = new BigInteger(130, secRand).toString(32);

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(randomPassword.toCharArray(), randomSalt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
