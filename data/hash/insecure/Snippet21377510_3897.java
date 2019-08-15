String modulusElem = "...";
String expElem = "...";

byte[] expBytes = Base64.decode(expElem, Base64.DEFAULT);
byte[] modulusBytes = Base64.decode(modulusElem, Base64.DEFAULT);

BigInteger modulus = new BigInteger(1, modulusBytes);
BigInteger exponent = new BigInteger(1, expBytes);

try {
    KeyFactory factory = KeyFactory.getInstance("RSA");

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

    String data = "my data";

    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] hashedData = md.digest(data.getBytes("UTF-8"));

    RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modulus, exponent);

    PublicKey publicKey = factory.generatePublic(pubSpec);

    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] SignedByteData = cipher.doFinal(hashedData);

} catch (Exception e){

}
