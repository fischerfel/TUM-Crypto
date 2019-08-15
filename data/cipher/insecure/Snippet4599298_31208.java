public static final Logger LOG = Logger.getLogger(CryptoUtil.class);

private Cipher cipher = null;

private SecretKey key = null;

// This variable holds a string based on which a unique key will be generated
private static final String SECRET_PHRASE = "SECRET PHRASE GOES HERE";

// Charset will be used to convert between String and ByteArray
private static final String CHARSET = "UTF8";

 // The algorithm to be used for encryption/decryption DES(Data Encryption Standard)
private static final String ALGORITHM = "DES";

public CryptoUtil() throws DDICryptoException {
    try {
        // generate a key from SecretKeyFactory
        DESKeySpec keySpec = new DESKeySpec(SECRET_PHRASE.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        key = keyFactory.generateSecret(keySpec);
        cipher = Cipher.getInstance(ALGORITHM);
    } catch (Exception e) {
        LOG.error(e);
        throw new DDICryptoException(e);
    }
}


/**
 * This method takes a plain text string and returns encrypted string using DES algorithm
 * @param plainText
 * @return String
 * @throws DDICryptoException
 */
public String encrypt(String plainText) throws DDICryptoException {
    String encryptedString = null;
    try {
        // initializes the cipher with a key.
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainTextAsUTF8 = plainText.getBytes(CHARSET);

        // decrypts data in a single-part or multi-part operation
        byte[] encryptedBytes = cipher.doFinal(plainTextAsUTF8);

        encryptedString = new sun.misc.BASE64Encoder().encode(encryptedBytes);
    } catch (Exception e) {
        LOG.error(e);
        throw new DDICryptoException(e);

    }
    return encryptedString;

}

/**
 * This method takes a plain text string and returns encrypted string using DES algorithm
 * @param encryptedString
 * @return
 * @throws DDICryptoException
 */
public String decrypt(String encryptedString) throws DDICryptoException {    
    String decryptedString = null;
    try {
        byte[] decodedString = new sun.misc.BASE64Decoder().decodeBuffer(encryptedString);

        // initializes the cipher with a key.
        cipher.init(Cipher.DECRYPT_MODE, key);

        // decrypts data in a single-part or multi-part operation
        byte[] decryptedBytes = cipher.doFinal(decodedString);
        decryptedString = new String(decryptedBytes, CHARSET);
    } catch (Exception e) {
        LOG.error(e);
        throw new DDICryptoException(e);
    }
    return decryptedString;
}
