int dkLen = 64;
int rounds = 1000;
PBEKeySpec keySpec = new PBEKeySpec("Some password".toCharArray(), "SomeSalt".getBytes(), rounds, dkLen * 8);
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] out = factory.generateSecret(keySpec).getEncoded();
