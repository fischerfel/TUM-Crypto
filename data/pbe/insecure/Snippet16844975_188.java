    // The password
    String passwordString = "foo";

    // Base64 encoded random bytes used for the SALT.
    String saltString = "1234567890";

    char[] passwordChars = passwordString.toCharArray();
    byte[] saltBytes = Base64.decode(saltString);

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, 1024, 128);
    SecretKey secret = factory.generateSecret(spec);

    System.out.println(Base64.encodeBytes(secret.getEncoded()));
