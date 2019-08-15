    public static byte[] RSADecrypt(final byte[] encryptedBytes)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Cipher cipher1 = Cipher.getInstance("RSA");

    System.out.println("RSA Encryption key: " + privateKey.getAlgorithm());
    System.out.println("RSA Encryption key: " + privateKey.getEncoded());

    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cipher1.doFinal(encryptedBytes);
    return decryptedBytes;
}
