    public static byte[] RSAEncrypt(final byte[] plain)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Cipher cipher = Cipher.getInstance("RSA");
    System.out.println("RSA Encryption key: " + publicKey.getAlgorithm());
    System.out.println("RSA Encryption key: " + publicKey.getEncoded());

    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBytes = cipher.doFinal(plain);
    return encryptedBytes;
}
