public void loadKey(File in, byte[] privateKey) throws GeneralSecurityException, IOException {

    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey pk = kf.generatePrivate(privateKeySpec);
    rsacipher.init(Cipher.DECRYPT_MODE, pk);

    aesKey = new byte[128/8];
    FileInputStream fis = new FileInputStream(in);
    CipherInputStream input = new CipherInputStream(fis, rsacipher);
    input.read(aesKey);
    aesKeySpec = new SecretKeySpec(aesKey, "AES");
    input.close();
    fis.close();
 } 
