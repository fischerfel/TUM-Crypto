SecureRandom rnd = new SecureRandom();
IvParameterSpec ivspec = new IvParameterSpec(rnd.generateSeed(16));
