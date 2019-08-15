private byte[] encryptrc4(String toEncrypt, String key) throws Exception{
        // create a binary key from the argument key (seed)
        SecureRandom sr = new SecureRandom(key.getBytes());

        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        kg.init(sr);
        SecretKey sk = kg.generateKey();

        // create an instance of cipher
        Cipher cipher = Cipher.getInstance("RC4");

        // initialize the cipher with the key
        cipher.init(Cipher.ENCRYPT_MODE, sk);

        // enctypt!
        byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

        return encrypted;
    }
