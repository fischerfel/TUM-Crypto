   public byte[] RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    kp = kpg.genKeyPair();
    publicKey = kp.getPublic();
    privateKey = kp.getPrivate();

    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(plain.getBytes());
    System.out.println("EEncrypted?????" + org.apache.commons.codec.binary.Hex.encodeHexString(encryptedBytes));
    return encryptedBytes;
}
