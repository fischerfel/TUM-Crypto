public byte[] encryptData(String base64) throws GeneralSecurityException {
    byte[] dataToEncrypt = base64.getBytes(Charset.forName("UTF-8")); //lenght == 90
    try {
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaKeys.getPublicModus(), rsaKeys.getPublicExpo());
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(rsaPublicKeySpec);

        //default providers
        //4.0.3 - BC (BouncyCastleProvider)
        //4.4.2 - AndroidOpenSSL
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encryptedData = cipher.doFinal(dataToEncrypt); //this throw exception

        return encryptedData;
    } catch (GeneralSecurityException e) {
        throw e;
    }
}
