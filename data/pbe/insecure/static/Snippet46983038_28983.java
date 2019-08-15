        int keySize = 16*8;
        int iterations = 800000;
        char[] password = "password".toCharArray();
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);

        SecretKeyFactory secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec spec = new PBEKeySpec(password, salt, iterations, keySize);
        SecretKey pbeSecretKey = secKeyFactory.generateSecret(spec);
        SecretKey desSecret = new SecretKeySpec(pbeSecretKey.getEncoded(), "DESede");

        // iv needs to have block size
        // we will use the salt for simplification
        IvParameterSpec ivParam = new IvParameterSpec(salt);

        Cipher cipher = Cipher.getInstance("DESEde/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,  desSecret, ivParam);

        System.out.println("salt: "+Base64.getEncoder().encodeToString(salt));
        System.out.println(cipher.getIV().length+" iv: "+Base64.getEncoder().encodeToString(cipher.getIV()));
        byte[] ciphertext = cipher.doFinal("plaintext input".getBytes());
        System.out.println("encrypted: "+Base64.getEncoder().encodeToString(ciphertext));
