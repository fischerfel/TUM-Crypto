    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF1Pkcs#5");
    KeySpec spec = new PBEKeySpec(password, salt, 1, 128);
