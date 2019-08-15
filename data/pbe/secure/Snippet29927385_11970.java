PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 1024, 256);
SecretKey secretKey = newSecretKey("PBKDF2WithHmacSHA1", keySpec);
SecretKeySpec secretKey = new SecretKeySpec(secretKey.getEncoded(), "AES");
