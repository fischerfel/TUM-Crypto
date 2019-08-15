public void encrypt(File inf, File outf, File publicKey, int userId, int resourceId) throws ArServerConnectionException {
    // ENCRYPTION BEGIN
    try {
        pkCipher = Cipher.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // create AES shared key cipher
    try {
        aesCipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        makeKey();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
            // File operation

            try {
        saveKey(new File(System.getProperty("user.home") + "/" + userId
                + "/keyfile"), publicKey);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (GeneralSecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // File operation

    try {
        encryptFiles(inf, outf);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    // /ENCRYPTION END
}

    public void saveKey(File out, File publicKeyFile) throws IOException,
        GeneralSecurityException {
    // read public key to be used to encrypt the AES key
    byte[] encodedKey = new byte[(int) publicKeyFile.length()];
    new FileInputStream(publicKeyFile).read(encodedKey);

    // create public key
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pk = kf.generatePublic(publicKeySpec);

    // write AES key
    pkCipher.init(Cipher.ENCRYPT_MODE, pk);
    CipherOutputStream os = new CipherOutputStream(
            new FileOutputStream(out), pkCipher);
    os.write(aesKey);
    os.close();
}
    public void makeKey() throws NoSuchAlgorithmException {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(AES_Key_Size);
    SecretKey key = kgen.generateKey();
    aesKey = key.getEncoded();
    aeskeySpec = new SecretKeySpec(aesKey, "AES");
}
