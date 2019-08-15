public class UserSMSVerifier {

static String signedMail;

static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
}


public static String messageGenarator(
        String UserCert,
        String origninalMessage         
        ) throws Exception{

    InputStream userCertStream = new ByteArrayInputStream(UserCert.getBytes("UTF-8"));

    PEMReader userCerti = new PEMReader(
              new InputStreamReader(
                      userCertStream));



    //KeyPair userPrivate = (KeyPair)userPrivateKey.readObject();
    X509Certificate userCert = (X509Certificate)userCerti.readObject();


    byte[] dectyptedText = null;
    // decrypt the text using the private key
    //modified by JEON 20130817
    //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, userCert.getPublicKey());
    dectyptedText = cipher.doFinal(origninalMessage.getBytes());

    String result = new String(dectyptedText, Charset.forName("UTF-8"));
    return result;

}


}
