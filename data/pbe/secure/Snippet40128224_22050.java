KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 100000, 64 * 8);
SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] hashedPassword = key.generateSecret(spec).getEncoded();
hashPassword = new String(Base64.encodeBase64(hashedPassword));
