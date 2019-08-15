String pubKeyPEM = "***";

public void something(){
  String sendToServer = Base64.encodeToString(RSAEncrypt("test"),0);
}

public byte[] RSAEncrypt(final String request) throws Exception {
    PublicKey publicKey = getPublicKey();
    cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(plain.getBytes());
}

public PublicKey getPublicKey() throws Exception {
    PublicKey publicKey;

    byte[] decoded = Base64.decode(pubKeyPEM, Base64.DEFAULT);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    publicKey = kf.generatePublic(new X509EncodedKeySpec(decoded));

    return publicKey;
}
