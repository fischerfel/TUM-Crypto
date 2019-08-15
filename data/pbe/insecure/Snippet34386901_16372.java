public static void savePrivateKeyToDisk(PrivateKey privateKey, String passord){

    try {
        // unencrypted PKCS#8 private key
        byte[] encodedPrivateKey = privateKey.getEncoded();

        String MYPBEALG = "PBEWithSHA1AndDESede";

        int count = 20;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher pbeCipher = Cipher.getInstance(MYPBEALG);

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Encrypt the encoded Private Key with the PBE key
        byte[] cipherText = pbeCipher.doFinal(encodedPrivateKey);

        // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
        AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
        algparms.init(pbeParamSpec);
        EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, cipherText);

        // DER encoded PKCS#8 encrypted key
        byte[] encryptedPkcs8 = encinfo.getEncoded();


        File encryptedPrivate = new File(PRIVATE_KEY_FILE);

        if (encryptedPrivate.getParentFile() != null) {
            encryptedPrivate.getParentFile().mkdirs();
        }
        encryptedPrivate.createNewFile();

        ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                new FileOutputStream(encryptedPrivate));
        publicKeyOS.writeObject(encryptedPkcs8);
        publicKeyOS.close();

    }
    catch (Exception e){
        e.printStackTrace();
    }
}
