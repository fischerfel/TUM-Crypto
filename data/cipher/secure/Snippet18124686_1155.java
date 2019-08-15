private static String decrypt(String pwd) {
    byte[] input = Base64.decode(pwd);


    try {
        rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (NoSuchPaddingException e) {
        return null;
    }

    KeyFactory keyFactory;
    try {
        keyFactory = KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
        return null;
    }

    String modString = "*********";
    String privateExponentString = "*********";

    RSAPrivateKeySpec prvKeySpec = new RSAPrivateKeySpec(new BigInteger(modString), new BigInteger(privateExponentString));
    RSAPrivateKey prvKey;


    try {
        prvKey = (RSAPrivateKey) keyFactory.generatePrivate(prvKeySpec);
    } catch (InvalidKeySpecException e) {
        return null;
    }

    try {
        rsaCipher.init(Cipher.DECRYPT_MODE, prvKey);
    } catch (InvalidKeyException e) {
        return null;
    }
    byte[] cipherText;
    try {
        cipherText = rsaCipher.doFinal(input);
    } catch (IllegalBlockSizeException e) {
        return null;
    } catch (BadPaddingException e) {
        return null;
    }
    return new String(cipherText);
}
