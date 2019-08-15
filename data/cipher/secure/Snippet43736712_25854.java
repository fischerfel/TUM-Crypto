The code for using .pem file below: 
public static String encrypt(String rawText, PublicKey publicKey) throws 
 IOException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes("UTF-
8")));
}

public static RSAPublicKey getPublicKey(String filename) throws IOException, 
GeneralSecurityException {
    String publicKeyPEM = getKey(filename);
    return getPublicKeyFromString(publicKeyPEM);
}

public static RSAPublicKey getPublicKeyFromString(String key) throws 
IOException, GeneralSecurityException {
    String publicKeyPEM = key;
    publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
    publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
    byte[] encoded = Base64.decodeBase64(publicKeyPEM);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new 
X509EncodedKeySpec(encoded));
    return pubKey;
}


private static String getKey(String filename) throws IOException {
    // Read key from file
    String strKeyPEM = "";
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line;
    while ((line = br.readLine()) != null) {
        strKeyPEM += line + "\n";
    }
    br.close();
    return strKeyPEM;
}

public static void main(String[] args) {
RSA necryptionService = new RSA();
try
   {
String file = "D:/public.pem";
                String data = "Hello Test"
       PublicKey publicKey = necryptionService.getPublicKey(file);
      String encryptedData = necryptionService.encrypt(data,publicKey);
       System.out.println(encryptedData); 
   }
   catch (Exception e)
   {
       e.printStackTrace();
   }
}

Where the string gets converted in the 3rd method, try this additional piece 
of code


    CertificateFactory fact = CertificateFactory.getInstance("X.509");
    FileInputStream is = new FileInputStream(path);
    Certificate cer = fact.generateCertificate(is);

    logger.info("Public Key:::::::::::loaded"+cer.getPublicKey());
