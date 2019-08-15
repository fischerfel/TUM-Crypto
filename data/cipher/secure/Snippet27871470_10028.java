try {
    KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
    keyStore.load(null);

    String alias = "key3";

    int nBefore = keyStore.size();

    // Create the keys if necessary
    if (!keyStore.containsAlias(alias)) {

        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        notAfter.add(Calendar.YEAR, 1);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
            .setAlias(alias)
            .setKeyType("RSA")
            .setKeySize(2048)
            .setSubject(new X500Principal("CN=test"))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(notBefore.getTime())
            .setEndDate(notAfter.getTime())
            .build();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        generator.initialize(spec);

        KeyPair keyPair = generator.generateKeyPair();
    }
    int nAfter = keyStore.size();
    Log.v(TAG, "Before = " + nBefore + " After = " + nAfter);

    // Retrieve the keys
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
    RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
    RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

    Log.v(TAG, "private key = " + privateKey.toString());
    Log.v(TAG, "public key = " + publicKey.toString());

    // Encrypt the text
    String plainText = "This text is supposed to be a secret!";
    String dataDirectory = getApplicationInfo().dataDir;
    String filesDirectory = getFilesDir().getAbsolutePath();
    String encryptedDataFilePath = filesDirectory + File.separator + "keep_yer_secrets_here";

    Log.v(TAG, "plainText = " + plainText);
    Log.v(TAG, "dataDirectory = " + dataDirectory);
    Log.v(TAG, "filesDirectory = " + filesDirectory);
    Log.v(TAG, "encryptedDataFilePath = " + encryptedDataFilePath);

    Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
    inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
    outCipher.init(Cipher.DECRYPT_MODE, privateKey);

    CipherOutputStream cipherOutputStream = 
        new CipherOutputStream(
            new FileOutputStream(encryptedDataFilePath), inCipher);
    cipherOutputStream.write(plainText.getBytes("UTF-8"));
    cipherOutputStream.close();

    CipherInputStream cipherInputStream = 
        new CipherInputStream(new FileInputStream(encryptedDataFilePath),
            outCipher);
    byte [] roundTrippedBytes = new byte[1000]; // TODO: dynamically resize as we get more data

    int index = 0;
    int nextByte;
    while ((nextByte = cipherInputStream.read()) != -1) {
        roundTrippedBytes[index] = (byte)nextByte;
        index++;
    }
    String roundTrippedString = new String(roundTrippedBytes, 0, index, "UTF-8");
    Log.v(TAG, "round tripped string = " + roundTrippedString);

} catch (NoSuchAlgorithmException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (NoSuchProviderException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (InvalidAlgorithmParameterException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (KeyStoreException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (CertificateException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (IOException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (UnrecoverableEntryException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (NoSuchPaddingException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (InvalidKeyException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (BadPaddingException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (IllegalBlockSizeException e) {
    Log.e(TAG, Log.getStackTraceString(e));
} catch (UnsupportedOperationException e) {
    Log.e(TAG, Log.getStackTraceString(e));
}
