SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), byteArraySalt, 25, 128);
SecretKey temp = factory.generateSecret(spec);
