public class Encrypter {

    private Cipher cipher; //The encryption cipher object
public static final String ALGORITHM = "Blowfish"; //Encryption Algorithm

    /**
     * Constructor
     */
    public Encrypter()
    {       
        try {
            initlizeCipher();
        } catch (Throwable e) {     
            ServerSettings.LOG.logError(e);
            e.printStackTrace();

        }
    }

    /**
     * Initialize the Cipher object
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private void initlizeCipher() throws NoSuchAlgorithmException, NoSuchPaddingException
    {       
        cipher = Cipher.getInstance(ServerSettings.ALGORITHM);
    }

    /**
     * Encrypt a String
     * @param string String to encrypt
     * @return an encrypted String
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public synchronized String encrypt(String string) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        cipher.init(Cipher.ENCRYPT_MODE, ServerSettings.SECRECT_KEY_SPEC);
        byte[] stringBytes = string.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(stringBytes);
        return Base64.encodeBytes(encryptedBytes);      
    }

    /**
     * Decrypt a String
     * @param string String to decrypt
     * @return a decrypted String
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public synchronized String decrypt(String string) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
    {       
        cipher.init(Cipher.DECRYPT_MODE, ServerSettings.SECRECT_KEY_SPEC);
        byte[] decryptedBytes = Base64.decode(string.getBytes());       
        byte[] encryptedBytes = cipher.doFinal(decryptedBytes);
        return new String(encryptedBytes,"UTF-8");
    }   
}
