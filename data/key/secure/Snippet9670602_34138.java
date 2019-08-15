private static final int INIT_VECTOR_LENGTH = 16;
private static final int PRIVATE_KEY_LENGTH = 128;
private static final int SALT_LENGTH = 16;
private static final int PBE_KEYSPEC_ITERATIONS = 65536;

private static final String CIPHER_ALGORITHM = "AES";
private static final String CIPHER_ALGORITHM_MODE = "CBC";
private static final String CIPHER_ALGORITHM_PADDING = "PKCS5Padding";
private static final String DIGEST = "SHA1";
private static final String PLAINTEXT_ENCODING = "UTF8";
private static final String PRNG = DIGEST + "PRNG";
private static final String SECRET_KEY_FACTORY = "PBKDF2WithHmac" + DIGEST;

private static final String CIPHER = CIPHER_ALGORITHM + "/" + CIPHER_ALGORITHM_MODE + "/" + CIPHER_ALGORITHM_PADDING;

private IvParameterSpec ivSpec;
private final BASE64Encoder encoder = new BASE64Encoder();
private final BASE64Decoder decoder = new BASE64Decoder();

private Cipher getCipher(SecretKey key, int mode) {

    Cipher cipher = null;

    try {
        cipher = Cipher.getInstance(CIPHER);
    }
    catch (NoSuchAlgorithmException e) {System.err.println(System.err.println(e.getMessage());}
    catch (NoSuchPaddingException e) {System.err.println(e.getMessage());}

    try {
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, key);
            AlgorithmParameters params = cipher.getParameters();
            ivSpec = params.getParameterSpec(IvParameterSpec.class);
        }
        else {
            /* This is my point-of-failure. */
            cipher.init(mode, key, ivSpec);
        }
    }
    catch (InvalidKeyException e) {System.err.println(e.getMessage());}
    catch (InvalidAlgorithmParameterException e) {System.err.println(e.getMessage());}
    catch (InvalidParameterSpecException e) {System.err.println(e.getMessage());}

    return cipher;

}

private SecurityData.Secrets generateSecrets(SecretKey decryptedKey, byte[] salt, String passphrase) {

    /* Generate a new key for encrypting the secret key. */
    byte[] raw = null;
    PBEKey key = null;
    PBEKeySpec password = new PBEKeySpec(passphrase.toCharArray(), salt, PBE_KEYSPEC_ITERATIONS, PRIVATE_KEY_LENGTH);
    SecretKeyFactory factory = null;
    byte[] initVector = null;
    byte[] secretKeyBytes = decryptedKey.getEncoded();

    try {
        factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        key = (PBEKey) factory.generateSecret(password);
    }
    catch (NoSuchAlgorithmException e) {System.err.println(e.getMessage());}
    catch (InvalidKeySpecException e) {System.err.println(e.getMessage());}

    SecretKeySpec newKey = new SecretKeySpec(key.getEncoded(), CIPHER_ALGORITHM);

    /* Encrypt the secret key. */
    IvParameterSpec ivSpec = new IvParameterSpec(initVector);
    Cipher cipher = getCipher(newKey, ivSpec, Cipher.ENCRYPT_MODE);

    try {
        raw = cipher.doFinal(secretKeyBytes);
    }
    catch (IllegalBlockSizeException e) {System.err.println(e.getMessage());}
    catch (BadPaddingException e) {System.err.println(e.getMessage());}

    return new SecurityData.Secrets(encoder.encode(concatByteArrays(initVector, raw)), joinByteArray(salt));

}

private SecretKey decryptSecretKey(String encryptedKey, String salt, String passphrase) {

    /* Get initialisation vector. */
    byte[] raw = null, decoded = null, initVector = new byte[INIT_VECTOR_LENGTH];
    try {
        decoded = decoder.decodeBuffer(encryptedKey);
    } catch (IOException e) {System.err.println(e.getMessage());}
    System.arraycopy(decoded, 0, initVector, 0, INIT_VECTOR_LENGTH);
    raw = new byte[decoded.length-INIT_VECTOR_LENGTH];
    System.arraycopy(decoded, INIT_VECTOR_LENGTH, raw, 0, decoded.length-INIT_VECTOR_LENGTH);
    IvParameterSpec ivSpec = new IvParameterSpec(initVector);

    /* Generate the key. */
    byte[] rawSalt = splitByteArrayString(salt);
    PBEKeySpec password = new PBEKeySpec(passphrase.toCharArray(), rawSalt, PBE_KEYSPEC_ITERATIONS, PRIVATE_KEY_LENGTH);
    SecretKeyFactory factory = null;
    PBEKey key = null;
    try {
        factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        key = (PBEKey) factory.generateSecret(password);
    }
    catch (NoSuchAlgorithmException e) {System.err.println(e.getMessage());}
    catch (InvalidKeySpecException e) {System.err.println(e.getMessage());}

    Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);

    /* Decrypt the message. */
    byte[] stringBytes = null;
    try {
        stringBytes = cipher.doFinal(raw);
    }
    catch (IllegalBlockSizeException e) {System.err.println(e.getMessage());}
    catch (BadPaddingException e) {System.err.println(e.getMessage());}

    /* Converts the decoded message to a String. */
    String clear = null;
    try {
        clear = new String(stringBytes, PLAINTEXT_ENCODING);
    }
    catch (UnsupportedEncodingException e) {System.err.println(e.getMessage());}

    return new SecretKeySpec(clear.getBytes(), CIPHER_ALGORITHM);

}
