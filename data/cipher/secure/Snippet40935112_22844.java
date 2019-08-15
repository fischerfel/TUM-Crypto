private static final Provider SECURITY_PROVIDER = new BouncyCastleProvider();

public byte[] decryptMessage(byte[] message) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("EC", SECURITY_PROVIDER);
    PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(
            IOUtils.toByteArray(getClass().getResourceAsStream("/key.pkcs8")));
    PrivateKey privKey = keyFactory.generatePrivate(privSpec);

    Cipher cipher = Cipher.getInstance("ECIESwithAES-CBC/NONE/PKCS5Padding", SECURITY_PROVIDER);
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] result = cipher.doFinal(message);
    return result;
}
