int keyLength = 160; // because SHA1 generates 160-bit hashes
int iterations = 20 * 1000; //standard is 2000 but let's be more secure here

KeySpec spec = new PBEKeySpec(password.toCharArray(), generateSalt(), iterations, keyLength);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] hash = keyFactory.generateSecret(spec).getEncoded();
