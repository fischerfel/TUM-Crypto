public void SecretKey(byte[] key) {
    char[] ca = null;
    PBEKeySpec keySpec = null;
    SecretKeyFactory factory = null;

    _secretKey = null;
    try {
            ca = new char[key.length];  
            for (int x = 0; x < key.length; x++) {
                    ca[x] = (char)(key[x]);
            }
            keySpec = new PBEKeySpec(ca, _salt, 1024, 256);
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            _secretKey = factory.generateSecret(keySpec);
    .
    .
    .
