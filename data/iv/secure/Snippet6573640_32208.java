// symmetric algorithm for data encryption
final String ALGORITHM = "AES";
// Padding for symmetric algorithm
final String PADDING_MODE = "/CBC/PKCS5Padding";
// character encoding
final String CHAR_ENCODING = "UTF-8";
// provider for the crypto
final String CRYPTO_PROVIDER = "Entrust";
// RSA algorithm used to encrypt symmetric key
final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
// symmetric key size (128, 192, 256) if using 192+ you must have the Java
// Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
// installed
int AES_KEY_SIZE = 256;

private byte[] encryptWithRSA(byte[] aesKey, X509Certificate cert)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // get the public key from the encryption certificate to encrypt with
    PublicKey pubKey = cert.getPublicKey();

    // get an instance of the RSA Cipher
    Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);

    // set the cipher to use the public key
    rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);

    // encrypt the aesKey
    return rsaCipher.doFinal(aesKey);
}

private AESEncryptedContents encryptWithAes(byte[] dataToEncrypt)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, NoSuchProviderException {
    // get the symmetric key generator
    KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
    keyGen.init(AES_KEY_SIZE); // set the key size

    // generate the key
    SecretKey skey = keyGen.generateKey();

    // convert to binary
    byte[] rawAesKey = skey.getEncoded();

    // initialize the secret key with the appropriate algorithm
    SecretKeySpec skeySpec = new SecretKeySpec(rawAesKey, ALGORITHM);

    // get an instance of the symmetric cipher
    Cipher aesCipher = Cipher.getInstance(ALGORITHM + PADDING_MODE,
            CRYPTO_PROVIDER);

    // set it to encrypt mode, with the generated key
    aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    // get the initialization vector being used (to be returned)
    byte[] aesIV = aesCipher.getIV();

    // encrypt the data
    byte[] encryptedData = aesCipher.doFinal(dataToEncrypt);

    // package the aes key, IV, and encrypted data and return them
    return new AESEncryptedContents(rawAesKey, aesIV, encryptedData);
}

private byte[] decryptWithAES(byte[] aesKey, byte[] aesIV,
        byte[] encryptedData) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException,
        BadPaddingException, UnsupportedEncodingException,
        NoSuchProviderException {
    // initialize the secret key with the appropriate algorithm
    SecretKeySpec skeySpec = new SecretKeySpec(aesKey, ALGORITHM);

    // get an instance of the symmetric cipher
    Cipher aesCipher = Cipher.getInstance(ALGORITHM + PADDING_MODE,
            CRYPTO_PROVIDER);

    // set it to decrypt mode with the AES key, and IV
    aesCipher.init(Cipher.DECRYPT_MODE, skeySpec,
            new IvParameterSpec(aesIV));

    // decrypt and return the data
    byte[] decryptedData = aesCipher.doFinal(encryptedData);

    return decryptedData;
}

private byte[] decryptWithRSA(byte[] encryptedAesKey, PrivateKey privKey)
        throws IllegalBlockSizeException, BadPaddingException,
        InvalidKeyException, NoSuchAlgorithmException,
        NoSuchPaddingException, NoSuchProviderException {
    // get an instance of the RSA Cipher
    Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM, CRYPTO_PROVIDER);

    // set the cipher to use the public key
    rsaCipher.init(Cipher.DECRYPT_MODE, privKey);

    // encrypt the aesKey
    return rsaCipher.doFinal(encryptedAesKey);
}
