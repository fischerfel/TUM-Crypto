public byte[] rsaEncrypt(byte[] data) {

    byte[] cipherData;

    try {

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(pubMod, pubExp);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        cipherData = cipher.doFinal(data);
        return cipherData;

    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | BadPaddingException e) {
        e.printStackTrace();
    }

    return null;
}
