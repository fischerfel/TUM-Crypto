public static synchronized byte[] encryptPrivateKey(
        ElGamalPrivateKey privateKey, byte[] hashedAnsBytes)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, NoSuchProviderException, IOException {
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec key = new SecretKeySpec(hashedAnsBytes, "SHA256");
    c.init(Cipher.ENCRYPT_MODE, key);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(bos);
    out.writeObject(privateKey);
    out.close();
    bos.close();
    byte[] pvtKeyBytes = c.doFinal(bos.toByteArray());
    return pvtKeyBytes;
}
