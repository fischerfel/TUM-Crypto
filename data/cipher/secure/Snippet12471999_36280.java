    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte [] encryptedBytes,decryptedBytes;
    Cipher cipher,cipher1;
    String encrypted,decrypted;

    public String RSAEncrypt (final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        encrypted = new String(encryptedBytes);
        System.out.println("EEncrypted?????"+encrypted);
        return encrypted;

    }

    public String RSADecrypt (final String result) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {

        cipher1=Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(result.getBytes());
        decrypted = new String(decryptedBytes);
        System.out.println("DDecrypted?????"+decrypted);
        return decrypted;

    }
