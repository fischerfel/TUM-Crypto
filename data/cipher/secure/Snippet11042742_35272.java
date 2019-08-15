public static String encrypt(String value, String key) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
    X509EncodedKeySpec x509 = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(key));
    KeyFactory factory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = factory.generatePublic(x509);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    bytes = cipher.doFinal(bytes);
    return DatatypeConverter.printBase64Binary(bytes);
}
