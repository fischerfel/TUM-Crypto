public byte[] decryptRSA(String inputData) throws Exception {
    byte[] inputBytes = Base64.decodeBase64(inputData);
    PrivateKey key = loadPrivateKey(getPrivateKey());
    Cipher cipher1 = Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, key);
    return cipher1.doFinal(inputBytes);
}

private PrivateKey loadPrivateKey(String key64) throws Exception {
    byte[] pkcs8EncodedBytes = Base64.decodeBase64(key64);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
}
