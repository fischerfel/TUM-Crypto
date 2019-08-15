public class SignMail {

    static {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public static String sign(String userOriginalMessage) throws Exception {

        PEMReader userPrivateKey = new PEMReader(
          new InputStreamReader(
             new FileInputStream(Environment.getExternalStorageDirectory()+"/pkcs10priv.key")));

        KeyPair keyPair = (KeyPair)userPrivateKey.readObject();

        byte[] cipherText;
        //modified by JEON 20130817
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //encrypt the message using private key
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        cipherText = cipher.doFinal(userOriginalMessage.getBytes());
        return new String(Hex.encode(cipherText));

    }


}
