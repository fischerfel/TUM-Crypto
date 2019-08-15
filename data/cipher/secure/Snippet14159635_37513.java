@Test
public void simpleEncryptDecryptTest_shouldSucceed() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String text = "ASDF-asdföjk_\n394ysf";
    String encryptedText = null;


    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    PEMReader in = new PEMReader(new FileReader("C:/Users/User/tu/vs_exc3/keys/auction-server.pem"), new PasswordFinder() {
        @Override
        public char[] getPassword() {
            return new char[] {'2', '3', '4', '5', '6'};
        }
    });

    PrivateKey privateKey = (PrivateKey)in.readObject();

    in = new PEMReader(new FileReader("C:/Users/User/tu/vs_exc3/keys/auction-server.pub.pem"));


    PublicKey publicKey = (PublicKey)in.readObject();

    Cipher decodeCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
    Cipher encodeCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
    decodeCipher.init(Cipher.DECRYPT_MODE, privateKey);
    encodeCipher.init(Cipher.ENCRYPT_MODE, publicKey);


    byte[] encrypted = encodeCipher.doFinal(text.getBytes());
    encryptedText = new String(encrypted);
    byte[] decrypted = decodeCipher.doFinal(encryptedText.getBytes());

    Assert.assertTrue(text.equals(new String(decrypted)));
}
