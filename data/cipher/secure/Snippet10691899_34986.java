public String decryptText (byte[] encryptedText) throws Exception {

    String decryptedText = null;

    if (rsaData != null) {
        byte[] modBytes = Base64.decodeBase64(rsaData.getModulus().trim());
        byte[] dBytes = Base64.decodeBase64(rsaData.getD().trim());

        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger d = new BigInteger(1, dBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");

        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        PrivateKey privKey = factory.generatePrivate(privSpec);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decrypted = cipher.doFinal(encryptedText);

        decryptedText = new String(decrypted);

    }

    return decryptedText;
}
