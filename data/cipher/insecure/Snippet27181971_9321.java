    public static byte[] encrypt(byte[] plaintext, String key) throws Exception {
        char[] password = key.toCharArray();
        byte[] salt = "12345678".getBytes();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] ciphertext = cipher.doFinal(plaintext);  
        return ciphertext;
    }

    public static byte[] decrypt(byte[] ciphertext, String key) throws Exception {
        char[] password = key.toCharArray();
        byte[] salt = "12345678".getBytes();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] plaintext = cipher.doFinal(ciphertext);  
        return plaintext;
    }
