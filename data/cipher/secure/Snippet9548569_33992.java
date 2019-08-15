    public static void main(String[] args) throws Exception {
    byte[] dataToEncrypt = "Hello World!".getBytes("UTF-16LE");
    byte[] cipherData = null;
    byte[] decryptedData;

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(512);
    KeyPair kp = kpg.genKeyPair();
    Key publicKey = kp.getPublic();
    Key privateKey = kp.getPrivate();
    KeyFactory fact = KeyFactory.getInstance("RSA");

    RSAPublicKeySpec pub = (RSAPublicKeySpec) fact.getKeySpec(publicKey,
            RSAPublicKeySpec.class);

    RSAPublicKeySpec spec = new RSAPublicKeySpec(pub.getModulus(), pub
            .getPublicExponent());
    KeyFactory factory = KeyFactory.getInstance("RSA");

    PublicKey publicKeyRSA = factory.generatePublic(spec);
    Cipher cipher = Cipher.getInstance("RSA");

    int k = 0;
    for (int i = 0; i < 100; i++) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKeyRSA);
            cipherData = cipher.doFinal(dataToEncrypt);
        } catch (Exception e1) {
            System.out.println("Encrypt error");
        }

        String s = new String(cipherData, "UTF-16LE");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        try {
            decryptedData = cipher.doFinal(s.getBytes("UTF-16LE"));
            System.out.println("Decrypted: "
                    + new String(decryptedData, "UTF-16LE"));
            k += 1;
        } catch (Exception e1) {
            System.out.println("Decrypt error");
        }
    }
    System.out.println("Number of correct decryptions is: " + k);

}
