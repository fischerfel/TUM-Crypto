   private void loadKeys() {

    logger.info("In loadKeys method at "+new Date());
    try {
        char password[] = hsmServiceAppProps.getDigiSigPfxPassword().toCharArray();
        Provider userProvider = new sun.security.pkcs11.SunPKCS11(this.getClass().getClassLoader().getResourceAsStream("/pkcs11.cfg"));
        Security.addProvider(userProvider);
        KeyStore ks = KeyStore.getInstance("PKCS11");
        ks.load(null, password);

        String alias = null;
        /*X509Certificate userCert = null;
        PrivateKey userCertPrivKey = null;
        PublicKey userCertPubKey = null;
        Enumeration<String> e = ks.aliases();
        while (e.hasMoreElements()) {
            alias = (String) e.nextElement();
            logger.info("Alias of the e-Token : " + alias);
            userCert = (X509Certificate) ks.getCertificate(alias);
            userCertPubKey = (PublicKey) ks.getCertificate(alias).getPublicKey();
            userCertPrivKey = (PrivateKey) ks.getKey(alias, password);
        }*/
        alias = "*************************************";

        //X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
        publicKey = (PublicKey) ks.getCertificate(alias).getPublicKey();
        privateKey = (PrivateKey) ks.getKey(alias, password);

    } catch (Exception e) {
        logger.error("Error while getting public and private keys ->> ",e);
    }
}

private String performEncryption(String content,PublicKey publicKey) throws Exception {
    logger.debug("Encrypting using public key : "+content);
    Cipher publicEncryptCipher = Cipher.getInstance("RSA");
    publicEncryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBinaryData = publicEncryptCipher.doFinal(content.getBytes());
    Base64 encoder = new Base64();
    String encodedEncryptedContent =  new String(encoder.encode(encryptedBinaryData),"UTF-8");
    logger.debug("Encrypted Content ->> "+encodedEncryptedContent);
    return encodedEncryptedContent;
}

private String performDecryption(String encodedEncryptedContent, PrivateKey privateKey) throws Exception {
    logger.debug("Decrypting with private key ->> "+encodedEncryptedContent);
    Base64 decoder = new Base64();
    byte[] encryptedString = decoder.decode(encodedEncryptedContent.getBytes());
    Cipher privateDecryptCipher = Cipher.getInstance("RSA");
    privateDecryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBinaryData = privateDecryptCipher.doFinal(encryptedString);
    String decryptedContent = new String(decryptedBinaryData,"UTF-8");
    logger.debug("Decrypted Content ->> "+decryptedContent);
    return decryptedContent;
}
