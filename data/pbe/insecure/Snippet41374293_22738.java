SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
currentSpec = new PBEKeySpec("password", "salt".getBytes(),1000  , 128);
