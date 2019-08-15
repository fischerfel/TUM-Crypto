public static String encryptRSA(String text) throws Exception {

    byte[] encoded = Base64.getDecoder().decode(RSA_PUBLIC_KEY);

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");

    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    byte[] encrypted = cipher.doFinal(text.getBytes());

    return Base64.getEncoder().encodeToString(encrypted);
}
