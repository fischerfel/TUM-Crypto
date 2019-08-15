SecureRandom random = new SecureRandom();
byte[] keyBytes = new byte[32]; //32 Bytes = 256 Bits
random.nextBytes(keyBytes);
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
