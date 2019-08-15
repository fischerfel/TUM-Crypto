    final CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
    keypair.generate(1024);
    final PrivateKey privKey = keypair.getPrivateKey();
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
    EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms,ciphertext);
    byte[] encryptedPkcs8 = encinfo.getEncoded();

    // Now I am writing the encrypted private key into a file.
    // Using FileOutputStream 

    FileOutputStream out = new FileOutputStream("usingOutEncrypedPrivkey");
    out.write(Base64.encodeBase64(encryptedPkcs8, true));
    out.flush();
    out.close();

    // Using PrintWriter 
    PrintWriter pw = new PrintWriter("usingPwEncryptedPrivKey");
    pw.println("-----BEGIN "+ privKey.getAlgorithm() + " PRIVATE KEY-----");
    pw.println(Base64.encodeBase64(encryptedPkcs8));
    pw.println("-----END "+ privKey.getAlgorithm() +" PRIVATE KEY-----");
    pw.close();
