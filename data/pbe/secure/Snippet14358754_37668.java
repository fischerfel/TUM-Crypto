char[] passwordAsCharArray = password.toCharArray();
PBEKeySpec pbeKeySpec = new PBEKeySpec(passwordAsCharArray, salt, 1000, 256); 
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
SecretKeySpec secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES"); 
