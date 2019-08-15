public String getData() {
    String dataDecrypted = null;
    try {
        byte[] cryptionKeyCrypted = Base64.decode(cryptionKeyCryptedBase64, Base64.NO_WRAP);
        byte[] cryptionIV = Base64.decode(cryptionIVBase64, Base64.NO_WRAP);

        Cipher cipherRSA = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        // get private key from the pair used to grab the public key to send to the api
        cipherRSA.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivateKey());
        byte[] key = cipherRSA.doFinal(cryptionKeyCrypted);

        byte[] dataCrytped = Base64.decode(dataCryptedBase64, Base64.NO_WRAP);
        IvParameterSpec ivSpec = new IvParameterSpec(cryptionIV);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipherAES = Cipher.getInstance("AES/CBC/ZeroBytePadding");
        cipherAES.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] decryptedAESBytes = cipherAES.doFinal(dataCrytped);
        dataDecrypted = new String(decryptedAESBytes, "UTF-8");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return dataDecrypted;
}
