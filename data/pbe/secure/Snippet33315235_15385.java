int dkLen = 16;
int rounds = 1000;
PBEKeySpec keySpec = new PBEKeySpec(hashKey.toCharArray(),salt.getBytes(), rounds, dkLen * 8);
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
return factory.generateSecret(keySpec).getEncoded();
