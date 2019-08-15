byte[] salt = new byte[256 / Byte.SIZE];
SecureRandom defaultRNG = new SecureRandom();
defaultRNG.nextBytes(salt);

SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec ks = new PBEKeySpec(password,salt,2000,256);
SecretKey s = f.generateSecret(ks);
final byte[] result = s.getEncoded();
