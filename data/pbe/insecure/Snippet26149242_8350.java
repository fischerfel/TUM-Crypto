KeySpec spec = new PBEKeySpec(password.toCharArray(), "SALTSALT".getBytes(), 20000, 160);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] encoded = f.generateSecret(spec).getEncoded();
