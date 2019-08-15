byte[] randomKey = new byte[20];
SecureRandom rng = new SecureRandom();
rng.nextBytes(randomKey);
SecretKey hmacKey = new SecretKeySpec(randomKey, "HMAC");

KeyInfoFactory kif = fac.getKeyInfoFactory();
KeyName kv = kif.newKeyName("owlstead");
KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv)); 
