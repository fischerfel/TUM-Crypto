    final String suchAlphabet = "abcdefghijklmnopqrstuvwxyz";

    KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
    keyStore.load(null);

    /*
    KEY GENERATION
     */

    // Define the key spec
    KeyGenParameterSpec aesSpec = new KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setKeySize(128)
            .build();

    // Create the secret key in the key store
    KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
    keyGenerator.init(aesSpec);
    keyGenerator.generateKey();

    Cipher cipher;
    SecretKey secretKey;

    /*
    ENCRYPTION
     */

    // Load the secret key and encrypt
    secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(ALIAS, null)).getSecretKey();
    cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
    cipherOutputStream.write(suchAlphabet.getBytes());
    cipherOutputStream.flush();
    cipherOutputStream.close();

    /*
    DECRYPTION
     */

    // Load the secret key and decrypt
    secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(ALIAS, null)).getSecretKey();

    // The following two lines attempt to represent real world usage in that the previous line loaded
    // the key from the store and the next two lines attempt to create the cipher and then initialize
    // the cipher such that an IV can be extracted as it does not seem that you can use the spec or the
    // parameters. Interestingly, the following two lines only 'half' such that a-p fail to decrypt and
    // q-z decrypt successfully 100% of the time. Leaving the lines commented results an in a successful
    // decryption of the alphabet but this is not a usable scenario
    //
    //        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    //        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    IvParameterSpec ivParameterSpec = new IvParameterSpec(cipher.getIV());
    cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

    byte[] in = new byte[suchAlphabet.getBytes().length];
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
    IOUtils.readFully(cipherInputStream, in);
    cipherInputStream.close();

    /*
    VERIFY
     */

    String muchWow = new String(in);
    assertEquals(suchAlphabet, muchWow);
