public class TLSDecryptTest {

Cipher Ecipher;
Cipher Dcipher;

public TLSDecryptTest(String pubpath, String privpath){
    byte[] publicKeyContentsAsByteArray;
    RSAPublicKey pubKey;
    try {
    this.Ecipher = Cipher.getInstance("RSA");
    String path1 = new String("C:\\Users\\peter.marino\\Desktop\\javapub.key");
    File pubFile = new File(path1);
    publicKeyContentsAsByteArray = new byte[(int)pubFile.length()];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pubFile));
        publicKeyContentsAsByteArray = new byte[(int)pubFile.length()];
        bis.read(publicKeyContentsAsByteArray);
        bis.close();

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(publicKeyContentsAsByteArray));
       pubKey = (RSAPublicKey) certificate.getPublicKey();
       this.Ecipher.init(Cipher.ENCRYPT_MODE, pubKey);
    } catch(Exception e) {
        System.out.println("Exception" + e);
    }

    try {
    this.Dcipher = Cipher.getInstance("RSA");
    String path2 = new String("C:\\Users\\peter.marino\\Desktop\\java.key");
    File privFile = new File(path2);
    byte[] privateKeyContentsAsByteArray = new byte[(int)privFile.length()];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(privFile));
        privateKeyContentsAsByteArray = new byte[(int)privFile.length()];
        bis.read(privateKeyContentsAsByteArray);
        bis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        KeySpec ks = new PKCS8EncodedKeySpec(privateKeyContentsAsByteArray);
        RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);
        System.out.println("PRIVATE KEY:::: " + new String(privKey.getEncoded()).equals(new String(privateKeyContentsAsByteArray)));
        this.Dcipher.init(Cipher.DECRYPT_MODE, privKey);
    } catch(Exception e) {
        System.out.println("Exception" + e);
    }

}

 public byte[] en(byte[] decryptedMessage) throws Exception {
     byte[] encryptedMessage = this.Ecipher.doFinal(decryptedMessage);
     //byte[] encryptedMessage = this.Ecipher.doFinal(decryptedMessage);
     return (encryptedMessage);

 }


 public byte[] de(byte[] encryptedMessage) throws Exception {
     byte[] decryptedMessage = this.Dcipher.doFinal(encryptedMessage);
     return (decryptedMessage);

 }

public static void main(String args[]) throws Exception{
    TLSDecryptTest t = new TLSDecryptTest(null,null);
    String s = ("Testing decryption.1Testing decryption.2Testing decryption.3Testing decryption.4");
    System.out.println("S: " + s);
    byte[] todo = s.getBytes();
    byte[] e = t.en(todo);
    String es = new String(e);
    System.out.println("E: " + es);
    byte[] d = t.de(e);
    String ds = new String(d);
    System.out.println("D: " + ds);
}
