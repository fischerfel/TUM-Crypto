public class EncryptDecrypt {
public void encryptDecriptKey(){
    String text = new String("Hello");
    byte[] cipherData = null;
    byte[] cipherData2 = null;
    BigInteger modulus = new BigInteger("E265721199CB82C4080E6E4B87368D01701A1171704629C66BA67DFF1127BB287F9494C8FE80D95D591FEE57D90D96359309AC650E7D0DF39FAAE366C5D5D89F58B4C21FDA80A2792700196FF5AF2635D89DAAF5E622FB84059DE0A4F23DC5F2895917879FC72AA7AF587361D7D3E8578B17BAA92E26D8637D9F8F7556B8FAC1E7A72AB6462EE192CC2ABE81A1E50FEAF9D2967F4DB92DDCA617272ED21A685B8896A9A2D34B24E723693865022B7776D134A041FA7ADFB1BB7B53C7AD891BB3A28E1AE7B399AB3CAC10E55432159A513FB24847129926A344C92DC984F002A97895373B353186E30A9CB33CA3DC7617A4FCBC0BAA1CCBF1E22286F1A9A09D89", 16);
    BigInteger pubExp = new BigInteger("65537", 16);
    BigInteger privateExp = new BigInteger("6274C5AFBBD7926DD8271676483E44022D135924A1341234D55A198F549197C61BFDACDAE03B7ECC26D7491AF12D04771613EDE220F3E79E5C80BFD6511117DCEC81E7AE5CA2F685839D7A728341017807554225204974624CE304F016DD2079C29B792D2522437D9B36F72EC4E2C637542924A73087FA31024FE2DE9FB1607323B8F98020FF0EEFE386DFEB77931C0146B60F757A30397955CF554D86654DD5E9AD600E7982FD59B052DB1014BC186ABE0E0D7250EBDD6BB789DC29DEA9ADCFD12D5713CFAC53FB28035C7F436EEE3519C13DC6516AAA549B07E190F6D4754ED4CF4953EE1773E6E091E93D3C34668FFFC6AA2230A8367DEA9BC348F335B925", 16);

    KeyFactory keyFactory = null;
    RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
    RSAPublicKey key = null;
    Cipher cipher = null;
    try {
        keyFactory = KeyFactory.getInstance("RSA");
        key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
        cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherData = cipher.doFinal(text.getBytes());
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }



    KeyFactory keyFactory2 = null;
    RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExp);
    RSAPrivateKey privatekey = null;
    Cipher cipher2 = null;
    try {
        keyFactory2 = KeyFactory.getInstance("RSA");
        privatekey = (RSAPrivateKey) keyFactory1.generatePrivate(privateKeySpec);
        cipher2 = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher2.init(Cipher.DECRYPT_MODE, privatekey);
        cipherData2 = cipher2.doFinal(cipherData);
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }

        System.out.println(Arrays.toString(text.getBytes()));
        System.out.println(Arrays.toString(cipherData2));
        //the two outputs should be equals
}
