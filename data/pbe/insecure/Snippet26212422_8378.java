    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    char[] passowrd = "secret".toCharArray();
    byte[] salt = "asdff8723lasdf(**923412".getBytes();

    KeySpec spec = new PBEKeySpec(passowrd,salt, 2000);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
