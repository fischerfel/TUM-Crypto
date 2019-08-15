PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 
    NUM_OF_ITERATIONS, KEY_SIZE);
SecretKeyFactory factoryKey = SecretKeyFactory.getInstance(PBE_ALGORITHM);
SecretKey tempKey = factoryKey.generateSecret(pbeKeySpec);
SecretKey secretKey = new SecretKeySpec(tempKey.getEncoded(), "AES");
