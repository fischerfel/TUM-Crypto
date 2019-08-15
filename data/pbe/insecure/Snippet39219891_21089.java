SecretKey key = keyFactory.generateSecret(new PBEKeySpec("PASSWORD".toCharArray(), SALT, 20));
