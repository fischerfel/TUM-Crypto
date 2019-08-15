public void EncryptUsingPublicKey(File in, File out, File publicKeyFile) throws IOException, GeneralSecurityException {

    byte[] encodedKey = new byte[(int)publicKeyFile.length()];
    new FileInputStream(publicKeyFile).read(encodedKey);

    // create public key
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pk = kf.generatePublic(publicKeySpec);

    FileInputStream is = new FileInputStream(in);
    Cipher pkCipher = Cipher.getInstance("RSA");
    pkCipher.init(Cipher.ENCRYPT_MODE, pk);
    CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher);
    copy(is, os);
    os.close();
}
