Random rand = new SecureRandom();
byte[] bytes = new byte[16];
rand.nextBytes(bytes);
IvParameterSpec ivSpec = new IvParameterSpec(bytes);
