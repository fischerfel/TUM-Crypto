PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] secretPassword = skf.generateSecret(spec).getEncoded();
