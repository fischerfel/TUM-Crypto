public static String encryptData(String textToEncrypt) {

    String modulus = "sAyRG6mbVY1XoPGZ9Yh+ZJvI40wxiq4LzoSbLlIdrYLelvzeQZD6Y6eG9XIALpEvnL3ZECf1Emnv17yELrcQ5w==";
    String exponent = "AQAB";

    try {

        byte[] modulusBytes = Base64.decode(modulus.getBytes("UTF-8"), Base64.DEFAULT);
        byte[] exponentBytes = Base64.decode(exponent.getBytes("UTF-8"), Base64.DEFAULT);

        BigInteger modulusInt = new BigInteger(1, modulusBytes);
        BigInteger exponentInt = new BigInteger(1, exponentBytes);

       /* RSAPrivateKeySpec rsaPrivKey = new RSAPrivateKeySpec(modulusInt, exponentInt);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privKey = fact.generatePrivate(rsaPrivKey);*/

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulusInt, exponentInt);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(rsaPubKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] encryptedByteData = cipher.doFinal(textToEncrypt.getBytes());

        return Base64.encodeToString(encryptedByteData, Base64.NO_WRAP);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
