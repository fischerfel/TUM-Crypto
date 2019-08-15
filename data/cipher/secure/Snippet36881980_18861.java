  public byte[] decryptUsingPrivateKey(String encryptedData, Context cntx) {

    byte[] utf8 = null;
    try {
        KeyStore.PrivateKeyEntry privateKey = getKeyFromFile(
                "PrivateKeyFile.pfx", "privatekeypassword", cntx);

        Cipher rsa;
        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsa.init(Cipher.DECRYPT_MODE, privateKey.getPrivateKey());
        utf8 = rsa.doFinal(Base64.decode(encryptedData));

    } catch (Exception e) {
        System.out.println(e);
    }

    return utf8;
}
