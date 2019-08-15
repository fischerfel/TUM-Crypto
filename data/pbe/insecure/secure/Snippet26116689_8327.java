private static byte[] passwordEncrypt(char[] password, byte[] plaintext) throws Exception {
    String MYPBEALG = "PBEWithSHA1AndDESede";

    int count = 20;// hash iteration count
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[8];
    random.nextBytes(salt);

    // Create PBE parameter set
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

    Cipher pbeCipher = Cipher.getInstance(MYPBEALG);

    // Initialize PBE Cipher with key and parameters
    pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    // Encrypt the encoded Private Key with the PBE key
    byte[] ciphertext = pbeCipher.doFinal(plaintext);

    // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
    AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
    algparms.init(pbeParamSpec);
    EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

    // and here we have it! a DER encoded PKCS#8 encrypted key!
    return encinfo.getEncoded();
