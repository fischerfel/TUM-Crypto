public byte[] encryptRSA(final InputStream publicKeyFile, String in) throws IOException, NoSuchAlgorithmException,
    InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,
    BadPaddingException {
    byte[] encodedKey = new byte[5000];
    publicKeyFile.read(encodedKey);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pkPublic = kf.generatePublic(publicKeySpec);
    // Encrypt
    Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
    return pkCipher.doFinal(in.getBytes());
}
