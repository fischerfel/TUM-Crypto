String alias = "alias";

//Create keystore
KeyStore ksName  = KeyStore.getInstance(KeyStore.getDefaultType());

//Make an empty store
ksName.load(null);

// insert .cer file path here
FileInputStream fis = new FileInputStream("C:\\cert\\certificate.cer");
BufferedInputStream bis = new BufferedInputStream(fis);

CertificateFactory cf = CertificateFactory.getInstance("X.509");

while (bis.available() > 0) 
{
    java.security.cert.Certificate cert = cf.generateCertificate(bis);
    ksName.setCertificateEntry(alias, cert);
}          

// retrieve public key from keystore
PublicKey pubKey = (PublicKey) ksName.getKey(alias, null);

String data = "... data to be encrypted ....";
String alg = "RSA/ECB/PKCS1Padding";
Cipher cipher = Cipher.getInstance(alg);
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte encryptedBytes[] = cipher.doFinal(data.getBytes());
