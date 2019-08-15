   SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
   KeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
   SecretKey theKey = factory.generateSecret(spec);
