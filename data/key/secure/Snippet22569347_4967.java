public class EncryptText {

    private final String SECRET_TEXT = "baamx2maeaacxbmc";
    private final String CIPHERTYPE = "AES/CBC/PKCS5Padding";
    private final String ALGORITHM = "AES";
    private final String ENCODING = "UTF-8";
    private final byte[] SECRET;
    private final SecretKeySpec key;
    private final byte[] initializationVector;
    private final IvParameterSpec ivspec;


    public EncryptText() throws UnsupportedEncodingException {
        SECRET = SECRET_TEXT.getBytes(ENCODING);
        key = new SecretKeySpec(SECRET, ALGORITHM);
        initializationVector = new byte[]
            {1, 4, 3, 4, 10, 125, 64, 105, 13, 17, 10, 1, 7, 13, 0, 12};
        ivspec = new IvParameterSpec(initializationVector);
    }

    public EncryptText(byte[] iv) throws UnsupportedEncodingException {
        SECRET = SECRET_TEXT.getBytes(ENCODING);
        key = new SecretKeySpec(SECRET, ALGORITHM);
        initializationVector = iv;
        ivspec = new IvParameterSpec(initializationVector);
    }


    public String encryptString(String string) throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, 
            BadPaddingException {

        byte[] inputBytes = string.getBytes(ENCODING);
        Cipher encryptCipher = Cipher.getInstance(CIPHERTYPE);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] encryptedData = encryptCipher.doFinal(inputBytes);
        return String.valueOf(new BigInteger(encryptedData));
    }

    public String decryptString(String string) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        BigInteger value = new BigInteger(string);
        Cipher decryptCipher = Cipher.getInstance(CIPHERTYPE);
        decryptCipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        byte[] decryptedData = decryptCipher.doFinal(value.toByteArray());
        return new String(decryptedData);        
    }

}
