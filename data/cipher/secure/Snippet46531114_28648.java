/// generate key pair
ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("prime256v1");
try 
{
    KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
    g.initialize(spec, new SecureRandom());
    KeyPair keyPair = g.generateKeyPair();
    privateKey = keyPair.getPrivate();
    publicKey = keyPair.getPublic();
    System.out.println("PublicKey:"+publicKey+"\n");
    System.out.println("PrivateKey:"+privateKey+"\n");

} 
catch (Exception e) 
{
    e.printStackTrace();
}
 try {
    Cipher c = Cipher.getInstance("ECIES",BouncyCastleProvider.PROVIDER_NAME);
    c.init(Cipher.ENCRYPT_MODE,publicKey);
    encodeBytes = c.doFinal(origin.getBytes());
    String encrypt = Base64.getEncoder().encodeToString(encodeBytes);
    System.out.println(encrypt);
} catch (Exception e) {
    e.printStackTrace();
}
