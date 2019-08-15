public class Content {

    @Element
    private String cryptionKeyCryptedBase64;

    @Element
    private String cryptionIVBase64;

    @Element
    private String dataCryptedBase64;

    @SuppressLint("TrulyRandom")
    public String getData() {
        String dataDecrypted = null;
        try {
            PRNGFixes.apply(); // fix TrulyRandom
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            KeyPair keypair = keygen.generateKeyPair();
            PrivateKey privateKey = keypair.getPrivate();

            byte[] cryptionKeyCrypted = Base64.decode(cryptionKeyCryptedBase64, Base64.DEFAULT);
            //byte[] cryptionIV = Base64.decode(cryptionIVBase64, Base64.DEFAULT);

            Cipher cipherRSA = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] key = cipherRSA.doFinal(cryptionKeyCrypted);

            byte[] dataCrytped = Base64.decode(dataCryptedBase64, Base64.DEFAULT);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipherAES = Cipher.getInstance("AES");
            cipherAES.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decryptedAESBytes = cipherAES.doFinal(dataCrytped);
            dataDecrypted = new String(decryptedAESBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataDecrypted;
    }
}
