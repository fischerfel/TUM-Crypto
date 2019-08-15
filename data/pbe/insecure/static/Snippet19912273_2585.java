public class testaes {
/**
 * The iteration count for key generation algorithm.
 */
private static final int KEY_ITERATION_COUNT = 12345;

/**
 * The key length in bits.
 */
private static final int KEY_LENGTH = 128;

/**
 * The algorithm for cipher initialization.
 */
private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

/**
 * The algorithm for key factory selection.
 */
private static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";

/**
 * The algorithm for key generation.
 */
private static final String KEY_ALGORITHM = "AES";

/**
 * The byte encoding.
 */
private static final String BYTE_ENCODING = "UTF-8";

private static testaes instance = null;


public testaes() {
    super();
}

public static testaes getInstance() {
    if (instance == null) {
        instance = new testaes();
    }
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    return instance;
}

/**
 * Instantiates the cipher.
 */
private Cipher initCipher(int opmode) throws Exception, NoSuchAlgorithmException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
    String access = "XXXXX";

    byte[] salt = "XXXXX".getBytes();

    String ivString = "XXXXX";


    // Build the key from password and salt.
    char[] accessCharArray = access.toCharArray();
    byte[] saltByteArray = Base64.decodeBase64(salt);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
    KeySpec spec = new PBEKeySpec(accessCharArray, saltByteArray, KEY_ITERATION_COUNT, KEY_LENGTH);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);

    // Create a cipher based on AES transformation.
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    // Initialize cipher to with Secret Key and IV.
    cipher.init(opmode, secretKey, new IvParameterSpec(ivString.getBytes(BYTE_ENCODING)));
    return cipher;
}

/**
 * Encrypts the data.
 * 
 * @param originalString
 *            The data to be encrypted.
 * @return The encrypted data as String.
 */
public String encryptAES(String originalString) {
    String encryptedString = null;
    try {
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes = cipher.doFinal(originalString.getBytes());
        String base64Encoded = new String(Base64.encodeBase64(encryptedBytes), Charset.forName(BYTE_ENCODING));
        String urlEncoded = URLEncoder.encode(base64Encoded, BYTE_ENCODING);
        encryptedString = urlEncoded;
    }  catch (Exception e) {
        e.printStackTrace();
    }
    return encryptedString;
}

/**
 * Decrypts the data.
 * 
 * @param encryptedString
 *            The encrypted data that is to be decrypted.
 * @return The decrypted (original) data as string.
 */
public String decryptAES(String encryptedString) {
    String decryptedString = null;

    try {
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
        String urlDecoded = URLDecoder.decode(encryptedString, BYTE_ENCODING);
        byte[] encryptedBytes = Base64.decodeBase64(urlDecoded.getBytes(Charset.forName(BYTE_ENCODING)));
        byte[] originalBytes = cipher.doFinal(encryptedBytes);
        decryptedString = new String(originalBytes);
    }  catch (Exception e) {
        e.printStackTrace();
    }
    return decryptedString;
}
