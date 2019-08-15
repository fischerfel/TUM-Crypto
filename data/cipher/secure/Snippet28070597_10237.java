public static String encryptData(String data, BigInteger modulus, BigInteger exponent) throws Exception {
    RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
    PublicKey pub = factory.generatePublic(spec);
    Cipher rsa = Cipher.getInstance("RSA", "BC");
    rsa.init(Cipher.ENCRYPT_MODE, pub);

    byte[] cipherText = rsa.doFinal(data.getBytes()); // ERROR HERE
    return Hex.toString(cipherText);
}
