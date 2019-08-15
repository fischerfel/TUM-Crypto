SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
SecretKey aesKey = factory.generateSecret(new PBEKeySpec(password, salt,
      iterations, 256));
