SecretKey sk = null;
KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, IT, KEY_LENGTH);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
sk = new SecretKeySpec(keyBytes, "AES");
