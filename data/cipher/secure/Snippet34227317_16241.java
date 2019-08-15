public TransactionData processData(TransactionData data) throws BTHException {
    String keystoreFilePath = manager.getStringValue(KeyStoreFilePath);
    String keystorePassword = manager.getStringValue(KeyStoreFilePassword);
    String privateKeyPassword = manager.getStringValue(KeyStorePrivateKeyPassword);
    String certificateAlias = manager.getStringValue(CertificateAlias);

    org.apache.xml.security.Init.init();

    try {
        InputStream in = data.getDataStream();
        byte[] dataBytes = DataUtil.readBytes(in);
        String encryptedDataStr = new String(dataBytes);

        PrivateKey privateKey = getPrivateKeyFromKeyStore(keystoreFilePath, keystorePassword, certificateAlias, privateKeyPassword);

        decrypt(
            encryptedDataStr,
            privateKey
        );
    }catch(Exception e){
        throw new BTHException(e.getMessage());
    }

    return data;
}

private PrivateKey getPrivateKeyFromKeyStore(String keyStoreFilePath, String keyStorePassword, String privateKeyCertAlias, String privateKeyPassword) throws BTHException {
    PrivateKey privateKey = null;
    try {
        KeyStore keystore = KeyStore.getInstance("JKS");
        BASE64Encoder encoder = new BASE64Encoder();
        keystore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());
        Key key=keystore.getKey(privateKeyCertAlias,keyStorePassword.toCharArray());
        if(key instanceof PrivateKey) {
            Certificate cert=keystore.getCertificate(privateKeyCertAlias);
            PublicKey publicKey=cert.getPublicKey();
            KeyPair keyPair = new KeyPair(publicKey,(PrivateKey)key);
            privateKey = keyPair.getPrivate();
        }
        //privateKeyEncoded = encoder.encode(privateKey.getEncoded());
    } catch (Exception e) {
        throw new BTHException(e.getMessage());
    }
    return privateKey;
}

private String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException, BTHException {
    String decryptedValue = null;

    try {
        // 1. Get the cipher ready to start doing the AES transformation
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // 2. Start the decryption process
        // THIS IS WHERE IT FAILS
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 3. Finish the decryption process
        decryptedValue = new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "UTF-8");
    } catch (Exception e) {
        throw new BTHException(e.getMessage());
    }

    return decryptedValue;
}
