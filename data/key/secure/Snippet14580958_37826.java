final CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
final X500Name x500Name =
    new X500Name("IN", "AP", "HYD", "TEST", "TEST_ORG", "test@xyz.com");
keypair.generate(1024);
final PrivateKey privKey = keypair.getPrivateKey();
final X509Certificate[] chain = new X509Certificate[1];
long validity = 123;
chain[0] = keypair.getSelfCertificate(x500Name, new Date(), 
    validity * 24 * 60 * 60);
Key key =  new SecretKeySpec(password.getBytes(), ALGO);
Cipher c = Cipher.getInstance(ALGO);
c.init(Cipher.ENCRYPT_MODE, key);
byte[] encVal = c.doFinal(privKey.getEncoded());
AlgorithmParameters params = AlgorithmParameters.getInstance("DES");
params.init(encVal); // <--- exception thrown here
EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(params, encVal);

// displaying encrypting value
String encryptedValue = Base64.encodeBase64String(encinfo.getEncoded());
System.out.println(encryptedValue);
