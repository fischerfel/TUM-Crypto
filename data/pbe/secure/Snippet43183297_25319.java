byte[] salt = new byte[8];
random.nextBytes(salt);
KeySpec spec = new PBEKeySpec(EncryptionKey.toCharArray(), salt, 65536, 128);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = f.generateSecret(spec).getEncoded();
