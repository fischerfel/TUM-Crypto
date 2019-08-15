public void init (String ID ) throws FileNotFoundException, IOException, Exception{

    Object o[] = openFile(ID+".pub");
    setPubMod((BigInteger) o[0]);
    setPubexp((BigInteger) o[1]);
    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(this.pubMod, this.pubexp);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    pubKey = (RSAPublicKey) fact.generatePublic(keySpec);
    o = openFile(ID+".priv");
    setPrivMod((BigInteger) o[0]);
    setPrivexp((BigInteger) o[1]);
    RSAPrivateKeySpec keySpec1 = new RSAPrivateKeySpec(this.privMod, this.privexp);
    fact = KeyFactory.getInstance("RSA");
    privKey = (RSAPrivateKey) fact.generatePrivate(keySpec1);
    cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING"); 
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
}
