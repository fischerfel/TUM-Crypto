private static void send() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] senderFileBytes = getFileBytes(senderPrivateKeyFile);
    byte[] receiverFileBytes = getFileBytes(receiverPublicKeyFile);
    byte[] plainTextFileBytes = getFileBytes(plainTextFile);
    byte[] senderPrivateKeyBytes = new byte[128];
    byte[] senderModulusBytes = new byte[128];
    byte[] receiverPublicKeyBytes = new byte[128];
    byte[] receiverModulusBytes = new byte[128];

    System.arraycopy(senderFileBytes, 0, senderModulusBytes, 0, 128);
    System.arraycopy(senderFileBytes, 128, senderPrivateKeyBytes, 0, 128);
    System.arraycopy(receiverFileBytes, 0, receiverModulusBytes, 0, 128);
    System.arraycopy(receiverFileBytes, 128, receiverPublicKeyBytes, 0, 128);

    SecureRandom random = new SecureRandom();
    byte[] aesKeyBytes = new byte[16];
    byte[] ivKeyBytes = new byte[16];
    random.nextBytes(aesKeyBytes); //These two are being concatenated 
    random.nextBytes(ivKeyBytes);  //And then encrypted with RSA

    //Relevant section
    BigInteger receiverPublicKeyInteger = new BigInteger(receiverPublicKeyBytes);
    BigInteger receiverModulusInteger = new BigInteger(receiverModulusBytes);
    RSAPublicKeySpec receiverPublicKeySpec = new RSAPublicKeySpec(receiverModulusInteger, receiverPublicKeyInteger);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
    RSAPublicKey receiverPublicKey = (RSAPublicKey) keyFactory.generatePublic(receiverPublicKeySpec);

    Cipher rsaCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
    rsaCipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
    byte[] aesIvBytes = concat(aesKeyBytes, ivKeyBytes);
    byte[] sessionCipher = rsaCipher.doFinal(aesIvBytes); //Error here

}
