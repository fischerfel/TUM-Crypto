   SecureRandom random = new SecureRandom();
   IvParameterSpec iv = new IvParameterSpec(random.generateSeed(16));
