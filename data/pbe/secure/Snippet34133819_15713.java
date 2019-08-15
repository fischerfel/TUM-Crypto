final int KEY_LENGTH = 256;
final SecretKeyFactory factory = SecretKeyFactory.getInstance("YourPreferredAlgorithm");
final SecretKey key = factory.generateSecret(new PBEKeySpec(pass, salt, iterations, KEY_LENGTH));
