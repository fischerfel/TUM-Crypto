byte[] salt = new byte[8];
new SecureRandom().nextBytes(salt);
PBEKeySpec spec = new PBEKeySpec(KEY.toCharArray(), salt, 10000, 128);
