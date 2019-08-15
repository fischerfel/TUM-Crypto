public class CryptoTest {
    public static void main(String[] args) throws Exception {
        PEMParser parser = new PEMParser(new FileReader("/tmp/prikey.pem"));
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter   converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair kp = converter.getKeyPair((PEMKeyPair) parser.readObject());
        RSAPublicKey pubkey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privkey = (RSAPrivateKey) kp.getPrivate();

        byte[] ct = Files.readAllBytes(Paths.get("/tmp/ciphertext"));

        Cipher oaepFromInit = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSpecified.DEFAULT);
        oaepFromInit.init(Cipher.DECRYPT_MODE, privkey, oaepParams);
        byte[] pt = oaepFromInit.doFinal(ct);
    }

}
