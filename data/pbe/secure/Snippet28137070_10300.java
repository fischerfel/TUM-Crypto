    SecretKey mKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(PasswordStr.toCharArray());
