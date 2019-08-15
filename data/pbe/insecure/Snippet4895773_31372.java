public class EncryptDecryptUtil {
    /** Encryption Cipher */
    private static Cipher ecipher;
    /** Decription Cipher */
    private static Cipher dcipher;

    private static Logger logger = Logger.getLogger(EncryptDecryptUtil.class);

    /**
     * Constructor used to create this object. Responsible for setting and initializing this object's encrypter and
     * decrypter Cipher instances given a Secret Key and algorithm.
     * 
     * @param key Secret Key used to initialize both the encrypter and decrypter instances.
     * @param algorithm Which algorithm to use for creating the encrypter and decrypter instances.
     */
    public EncryptDecryptUtil(SecretKey key, String algorithm) {
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
        try {
            ecipher = Cipher.getInstance(algorithm);
            dcipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }

    /**
     * Constructor used to create this object. Responsible for setting and initializing this object's encrypter and
     * decrypter Chipher instances given a Pass Phrase and algorithm.
     * 
     * @param passPhrase Pass Phrase used to initialize both the encrypter and decrypter instances.
     */
    public EncryptDecryptUtil(String passPhrase) {
        Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
        // 8-bytes Salt
        byte[] salt = { (byte) 0xB9, (byte) 0x8B, (byte) 0xD8, (byte) 0x31, (byte) 0x55, (byte) 0x24, (byte) 0xF3, (byte) 0x13 };

        // Iteration count
        int iterationCount = 19;

        try {
            // Generate the secret key associated to the passphrase.
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            // Get instance of the cipher
            ecipher = Cipher.getInstance("PBEWithMD5AndDES");
            dcipher = Cipher.getInstance("PBEWithMD5AndDES");

            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException e) {
            logger.error("during encrypter instantiation",e);
        } catch (InvalidKeySpecException e) {
            logger.error("during encrypter instantiation",e);
        } catch (NoSuchPaddingException e) {
            logger.error("during encrypter instantiation",e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("during encrypter instantiation",e);
        } catch (InvalidKeyException e) {
            logger.error("during encrypter instantiation",e);
        }
    }

    /**
     * Takes a single String as an argument and returns an Encrypted version of that String.
     * 
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     */
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new String( Base64.encode(enc), "UTF8");

        } catch (BadPaddingException e) {
            logger.error("during encryption : ",e);
        } catch (IllegalBlockSizeException e) {
            logger.error("during encryption : ",e);
        } catch (UnsupportedEncodingException e) {
            logger.error("during encryption : ",e);
        } 
        return new String();
    }


    /**
     * Takes a encrypted String as an argument, decrypts and returns the decrypted String.
     * 
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     */
    public String decrypt(String str) {
        byte[] dec = new byte[0];
        try {
            // Decode base64 to get bytes. Not sure to understand why.
            dec = Base64.decode(str) ;
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (BadPaddingException e) {
            logger.error("error during decryption. String to decode was : "+str + " byte array to decode was : "+ Arrays.toString(dec) ,e);
        } catch (IllegalBlockSizeException e) {
            logger.error("during decryption : ",e);
        } catch (UnsupportedEncodingException e) {
            logger.error("during decryption : ",e);
        }  
        return new String();
    }
}
