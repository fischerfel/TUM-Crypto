SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512") );
SecretKey key            = factory.generateSecret( new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength) ); 
