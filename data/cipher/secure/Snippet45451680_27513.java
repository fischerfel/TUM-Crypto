    public class EncryptionUtil {

    //public key url or location on the desktop for testing
    public static String publicKeyUrl1 ="/Users/vikrambahl/Work/gstn-keys/GSTN_G2B_SANDBOX_UAT_public.cer";
            private static String file;

    private static PublicKey readPublicKey(String filename) throws Exception
    {
        FileInputStream fin = new FileInputStream(filename);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        PublicKey pk = certificate.getPublicKey();
        return pk;

    }


    public static String encrypt(byte[] plaintext) throws Exception,NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {

        PublicKey key = readPublicKey(publicKeyUrl1);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByte= cipher.doFinal(plaintext);
        String encodedString = new String(java.util.Base64.getEncoder().encode(encryptedByte));
        return encodedString;
    }

    public static String generateEncAppkey(byte[] key){
        try {
            return encrypt(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }       
}

private static void produceSampleData(){
    try {
        //Generation of app key. this will be in encoded.
        String appkey = generateSecureKey();
        System.out.println("App key in encoded : "+ appkey);
        //Encrypt with GSTN public key
        String encryptedAppkey = EncryptionUtil.generateEncAppkey(decodeBase64StringTOByte(appkey));
        System.out.println("Encrypted App Key ->"+ encryptedAppkey);    
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
