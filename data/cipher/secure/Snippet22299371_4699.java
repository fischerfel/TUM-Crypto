private void stripHeaders(){

    public_key = public_key.replace("-----BEGIN PUBLIC KEY-----", "");
    public_key = public_key.replace("-----END PUBLIC KEY-----", "");

}

public byte[] encryptWithPublicKey(String encrypt) throws Exception {
    byte[] message = encrypt.getBytes("UTF-8");
    stripHeaders(); 
    PublicKey apiPublicKey= getRSAPublicKeyFromString(); 
    Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "SC");
    rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey); 
    return rsaCipher.doFinal(message);
}

private PublicKey getRSAPublicKeyFromString() throws Exception{
    KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SC"); 
    byte[] publicKeyBytes = Base64.decode(public_key.getBytes("UTF-8"), Base64.DEFAULT); 
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes); 
    return keyFactory.generatePublic(x509KeySpec);
}
