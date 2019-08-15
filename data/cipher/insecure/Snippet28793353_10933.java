public void encrypt(Cipher c0) throws IllegalBlockSizeException, IOException{
    /*
     * Creates unique AES key for encrypting the Object
     */
    KeyGenerator keyGen = null;
    try {
        keyGen = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    keyGen.init(256);
    keyCache = keyGen.generateKey();

    /*
     * Creating AES Cipher for encryption
     */
    Cipher c1 = null;
    try {
        c1 = Cipher.getInstance("AES");
        c1.init(Cipher.ENCRYPT_MODE, keyCache);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    /*
     * Encrypts Object
     */
    stack = new SealedObject(stackCache, c1);
    /*
     * Encrypts AES key with a Cipher given as argument (intended to be a public RSA key initialized Cipher)
     */
    key = new SealedObject(keyCache, c0);
}
