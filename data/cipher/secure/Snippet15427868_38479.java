public static String decrypt(InputStream stream, String encrypted) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    byte[] encodedKey = new byte[stream.available()];
    stream.read(encodedKey);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pkPublic = kf.generatePublic(publicKeySpec);

    Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    pkCipher.init(Cipher.DECRYPT_MODE, pkPublic);

    byte[] decoded = Base64Coder.decode(encrypted);
    byte[] decryptedInByte = pkCipher.doFinal(decoded);
    return new String(decryptedInByte);
}
