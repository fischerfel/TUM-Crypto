KeySpec spec = new PBEKeySpec(password.toCharArray(), encryptionKeySalt, 12345,256);
SecretKey encriptionKey = factory.generateSecret(spec);
