public String decrypte (String encCardNo) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    FileInputStream is = new FileInputStream("C:/Users/admin/Desktop/keystore/ksjksformat.jks");    
    String keystpassw = "9801461740";
    String alias = "ksjksformat";       
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());                      
        ks.load(is,keystpassw.toCharArray() );      
        Key key = ks.getKey(alias, keystpassw.toCharArray());
        Certificate cert = ks.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();
        new KeyPair(publicKey, (PrivateKey) (key));     
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystpassw.toCharArray());
        KeyStore.PrivateKeyEntry pkentry = (PrivateKeyEntry) ks.getEntry(alias, protParam);
        PrivateKey myPrivateKey =pkentry.getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, myPrivateKey); 
        byte[] decoded = Base64.decodeBase64(encCardNo);        
        byte[] cipherData = cipher.doFinal(decoded);
        return new String(cipherData);      
}`
