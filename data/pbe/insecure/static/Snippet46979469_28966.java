SecureRandom saltRand = new SecureRandom(new byte[] { 1, 2, 3, 4 });
byte[] salt = new byte[16];
saltRand.nextBytes(salt);

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 1024, 128);
SecretKey key = factory.generateSecret(spec);

SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
sr.setSeed(key.getEncoded());
KeyGenerator kg = KeyGenerator.getInstance("AES");
kg.init(128, sr);
sksCrypt = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
