SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, 1000, 384);
Key secretKey = factory.generateSecret(pbeKeySpec);
byte[] key = new byte[32];
byte[] iv = new byte[16];
System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);
