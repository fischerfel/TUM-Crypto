    private static String rsaEncryptPassword(String modulus, String exponent, String password) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    byte[] modulusBytes = Base64.decodeBase64(modulus.getBytes());
    byte[] exponentBytes = Base64.decodeBase64(exponent.getBytes());

    BigInteger modulusInt = new BigInteger(1, modulusBytes);
    BigInteger exponentInt = new BigInteger(1, exponentBytes);

    RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulusInt, exponentInt);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey pubKey = fact.generatePublic(rsaPubKey);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    byte[] cipherData = cipher.doFinal(password.getBytes());

    byte[] encryptedBytes = Base64.encodeBase64(cipherData);

    String encryptedString = new String(encryptedBytes);
    System.out.println(encryptedString);

    return encryptedString;
}
