    byte[] encodedprivkey = privKey.getEncoded();
    String MYPBEALG = "PBEWithSHA1AndDESede";
    String password = "test123";

    int count = 20;// hash iteration count
    Random random = new Random();
    byte[] salt = new byte[8];
    random.nextBytes(salt);

    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());

    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

    Cipher pbeCipher = Cipher.getInstance(MYPBEALG);
    // Initialize PBE Cipher with key and parameters
    pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    // Encrypt the encoded Private Key with the PBE key
    byte[] ciphertext = pbeCipher.doFinal(encodedprivkey);


    // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
    AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
    algparms.init(pbeParamSpec);
    EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

    FileOutputStream out3 = new FileOutputStream("server.key");
    out3.write(Base64.encodeBase64(encryptedPkcs8, true));
    out3.flush();
    out3.close();


    FileOutputStream out3 = new FileOutputStream("server.crt");
    out3.write(Base64.encodeBase64(chain[0].getEncoded(), true));
    out3.flush();
    out3.close();
