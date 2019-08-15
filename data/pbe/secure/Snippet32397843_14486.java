public PrivateKey decryptKey(byte[] pkcs8Data, char[] password) throws Exception {
    PBEKeySpec pbeSpec = new PBEKeySpec(password);
    EncryptedPrivateKeyInfo pkinfo = new EncryptedPrivateKeyInfo(pkcs8Data);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(pkinfo.getAlgName());
    Key secret = skf.generateSecret(pbeSpec);
    PKCS8EncodedKeySpec keySpec = pkinfo.getKeySpec(secret);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
}
