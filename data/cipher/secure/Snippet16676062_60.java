public class Cryptotests {

    public static final String ALGORITHM = "RSA";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            init();
            KeyPair kp = generateKey();
            byte[] enc = encrypt("The Fat Cat Jumped Over the Bat".getBytes("UTF8"), kp.getPublic());
            byte[] dec = decrypt(enc, kp.getPrivate());
        } catch (Exception ex) {
            Logger.getLogger(Cryptotests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    /**
     * Encrypt a text using public key.
     *
     * @param text The original unencrypted text
     * @param key The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(byte[] text, PublicKey key) throws Exception {

        byte[] cipherText = null;
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance(
                "RSA / ECB / PKCS1Padding");
        System.out.println(
                "nProvider is:" + cipher.getProvider().getInfo());

        // encrypt the plaintext using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
        return cipherText;

    }

    /**
     * Decrypt text using private key
     *
     * @param text The encrypted text
     * @param key The private key
     * @return The unencrypted text
     * @throws java.lang.Exception
     */
    public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception {

        byte[] dectyptedText = null;
        // decrypt the text using the private key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return dectyptedText;
    }
}
