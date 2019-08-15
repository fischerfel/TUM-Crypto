public class RSACrypto {

    private static SecureRandom sr = new SecureRandom();

    /**
     * @param rsabits
     * @return keyPair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair newKeyPair(int rsabits) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(rsabits, sr);
        return generator.generateKeyPair();
    }

    /**
     * @param key
     * @return key
     */
    public static byte[] pubKeyToBytes(PublicKey key) {
        return key.getEncoded(); // X509 for a public key
    }

    /**
     * @param key
     * @return key
     */
    public static byte[] privKeyToBytes(PrivateKey key) {
        return key.getEncoded(); // PKCS8 for a private key
    }

    /**
     * @param bytes
     * @return key
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey bytesToPubKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }

    /**
     * @param bytes
     * @return key
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey bytesToPrivKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    /**
     * @param input
     * @param key
     * @return encryptedText
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public static byte[] encryptWithPubKey(byte[] input, PublicKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    /**
     * @param input
     * @param key
     * @return decryptedText
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public static byte[] decryptWithPrivKey(byte[] input, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    /**
     * @param plainText
     * @return encryptedText
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String plainText) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException {
        KeyPair kp = newKeyPair(1 << 11); // 2048 bit RSA; might take a second to generate keys
        PublicKey pubKey = kp.getPublic();
        PrivateKey priKey = kp.getPrivate();
        System.out.println("Private Key: " + new BASE64Encoder().encode(privKeyToBytes(priKey)));
        byte[] cipherText = encryptWithPubKey(plainText.getBytes("UTF-8"), pubKey);
        return new BASE64Encoder().encode(cipherText);
    }

    /**
     * @param encrypted
     * @param privateKey
     * @return decryptedText
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     */
    public static String decrypt(String encrypted, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException {
        PrivateKey privateKeyValue = bytesToPrivKey(new BASE64Decoder().decodeBuffer(privateKey));
        return new String(decryptWithPrivKey(new BASE64Decoder().decodeBuffer(encrypted), privateKeyValue), "UTF-8");
    }
}
