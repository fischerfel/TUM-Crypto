// Encryption code
protected byte[] encryptMessageWithPrivateKey(String aMessage) throws Exception {
    ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
    ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
    byte[] vEncoded = null;

    DataOutputStream outStream = new DataOutputStream(inputStream);
    outStream.writeBytes(Integer.toString(this.randomGen.nextInt()));
    outStream.writeBytes(":");

    outStream.writeBytes(this.mTimestampFormat.format(Long.valueOf(System.currentTimeMillis())));
    outStream.writeBytes(":");

    outStream.writeBytes(",");

    outStream.writeBytes(aMessage);

    DataOutputStream resultWriter = new DataOutputStream(resultStream);
    if (this.mEncryptCipher == null) {
        KeyStore privateKs = KeyStore.getInstance(KeyStore.getDefaultType());
        privateKs.load(new FileInputStream(new File(mPrivateCertFile)), null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) privateKs
                .getEntry(mPrivateCertKey, new KeyStore.PasswordProtection(
                        mPrivateCertPassword.toCharArray()));
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        System.out.println("encrypt private key : " + privateKey.getFormat());
        this.mEncryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        this.mEncryptCipher.init(1, privateKey);
        System.out.println("mEncryptCipher = " + this.mEncryptCipher);
    }

    byte[] vMyUnincryptedString = inputStream.toByteArray();

    resultWriter.write(this.mEncryptCipher.doFinal(vMyUnincryptedString));

    vEncoded = resultStream.toByteArray();

    System.out.println("Encrypt length : " + vEncoded.length);
    return vEncoded;
}

// Decyption code
protected byte[] decryptMessageWithPrivateKey(String aSecretMessage) throws Exception {
    System.out.println("aSecretMessage : " + aSecretMessage);
    byte[] vNoSecret = null;
    if (this.mDecryptCipher == null) {
        KeyStore privateKs = KeyStore.getInstance(KeyStore.getDefaultType());
        privateKs.load(new FileInputStream(new File(mPrivateCertFile)), null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) privateKs
                .getEntry(mPrivateCertKey, new KeyStore.PasswordProtection(
                        mPrivateCertPassword.toCharArray()));

        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        /*
         * KeyPair keyPair =
         * KeyPairGenerator.getInstance("RSA").generateKeyPair(); PublicKey
         * publicKey=keyPair.getPublic(); System.out.println("public key : "
         * +publicKey.toString());
         */

        System.out.println("privateKey = " + privateKey.getAlgorithm());

        this.mDecryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        /* .getInstance("RSA/ECB/PKCS1Padding"); */

        System.out.println("List of provider : ");
        Provider[] prov = Security.getProviders();
        for (Provider provider : prov) {
            System.out.println(" : " + provider.getName() + " ,Info : " + provider.getInfo());

        }

        this.mDecryptCipher.init(2, privateKey);
        System.out.println("mDecryptCipher = " + this.mDecryptCipher);
    }

    /* byte[]encryptText = HexToByte.hexStringToByteArray(aSecretMessage); */
    byte[] encryptText = TestConverter.afromHex(aSecretMessage);
    System.out.println("Encrypt lenght : " + encryptText);
    vNoSecret = this.mDecryptCipher.doFinal(encryptText);

    System.out.println("vNoSecret msg : " + vNoSecret.length);
    return vNoSecret;
}
