    private static SecureRandom random = new SecureRandom();

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    protected synchronized void generateKeys() throws InvalidKeyException, IllegalBlockSizeException, 
            BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, 
                NoSuchPaddingException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

        generator.initialize(256, random);

        KeyPair pair = generator.generateKeyPair();
        Key pubKey = pair.getPublic();
        Key privKey = pair.getPrivate();

        //store public key
        try {
            storeKey(pubKey, Constants.KEY_PATH.concat(Constants.SERVER_PREFIX.concat("-publickey")));
        } catch (Exception e) {
            e.printStackTrace();
            DBLogger.logMessage(e.toString(), Status.KEY_GENERATION_ERROR);
        } 

        //store private key
        try {
            storeKey(privKey, Constants.KEY_PATH.concat(Constants.SERVER_PREFIX.concat("-privatekey")));
        } catch (Exception e) {
            e.printStackTrace();
            DBLogger.logMessage(e.toString(), Status.KEY_GENERATION_ERROR);
        } 
    }

    protected synchronized String encryptUsingPublicKey(String plainText) throws IllegalBlockSizeException, BadPaddingException, 
        NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, 
            FileNotFoundException, IOException, ClassNotFoundException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, readKey(Constants.KEY_PATH.concat(Constants.SERVER_PREFIX.concat("-publickey"))), random);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        System.out.println("cipher: " + new String(cipherText));    

        return new String(cipherText);
    }

    protected synchronized String decryptUsingPrivatekey(String cipherText) throws NoSuchAlgorithmException, 
        NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, FileNotFoundException, 
            IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, readKey(Constants.KEY_PATH.concat(Constants.SERVER_PREFIX.concat("-privatekey"))));
        byte[] plainText = cipher.doFinal(cipherText.getBytes());
        System.out.println("plain : " + new String(plainText));

        return new String(plainText);
    }
    public static void main(String[] args) {
        KeyGenerator keyGenerator = new KeyGenerator();
        try {
            keyGenerator.deleteAllKeys(Constants.KEY_PATH);
            keyGenerator.generateKeys();

            String cipherText = keyGenerator.encryptUsingPrivateKey("dilshan");
            keyGenerator.decryptUsingPublickey(cipherText);

//          String cipherText = keyGenerator.encryptUsingPublicKey("dilshan1");
//          keyGenerator.decryptUsingPrivatekey(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            DBLogger.logMessage(e.toString(), Status.KEY_GENERATION_ERROR);
        }
    }
