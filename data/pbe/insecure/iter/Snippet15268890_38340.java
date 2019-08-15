int derivedKeyLength = 128;
int iterations = 500;
KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, derivedKeyLength);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] derivedKey = f.generateSecret(spec).getEncoded();    
