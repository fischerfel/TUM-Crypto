  byte[] keyBytes = new byte[16];
  new SecureRandom().nextBytes(keyBytes);
  SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");
