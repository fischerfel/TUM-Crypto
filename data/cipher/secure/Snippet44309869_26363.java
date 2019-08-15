    public class EncriptionRSAKey {
    public static final int RSA_Key_Size = 512; // dimensione chiave RSA
    private PrivateKey privKey;// chiave privata
    private PublicKey pubKey;// chiave pubblica

    /**
     * Costruttore che genera una chiave privata e una pubblica in RSA
     * @throws NoSuchAlgorithmException
     */
    public EncriptionRSAKey() throws NoSuchAlgorithmException{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(RSA_Key_Size);
        privKey = keyGen.genKeyPair().getPrivate();
        pubKey = keyGen.genKeyPair().getPublic();
    }


    /**
     * Decripta una chiave RSA cifrata con una chiave pubblica
     * @param data chiave AES
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public SecretKey decryptAESKey(byte[] data ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        SecretKey key = null;
        Cipher cipher = Cipher.getInstance("RSA");;
        cipher.init(Cipher.DECRYPT_MODE, privKey ); // inizializza il cipher
        key = new SecretKeySpec ( cipher.doFinal(data), "AES" ); // genera la chiave AES
        return key;
    }

    /**
     * Restituisce la chiave pubblica
     * @return pubKey chiave pubblica
     */
    public PublicKey getPubKey() {
        return pubKey;
    }
For AES key:

    public class EncriptionAESKey {
    public static final int AES_Key_Size = 256; // dimensione chiave AES

    /**
     * Genera una chiave AES
     * @return aesKey Chiave AES
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey makeAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_Key_Size); 
        return keyGen.generateKey();
    }

    /**
     * Cifra una chiave AES data una chiave pubblica RSA
     * @param skey Chiave AES da cifrare
     * @param publicKey Chiave RSA pubblica
     * @return key AES cifrato con RSA
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] EncryptSecretKey (SecretKey skey, PublicKey publicKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = null;
        byte[] key = null;
        // initialize the cipher with the user's public key
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey );
        key = cipher.doFinal(skey.getEncoded());
        return key;
    }
