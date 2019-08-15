SecureRandom secureRandom = new SecureRandom();
KeyGenerator kg = null;
kg = KeyGenerator.getInstance("AES");
kg.init(128, secureRandom);
SecretKey ClientSecretKey = kg.generateKey(); // Client AES is generated here.

String ServerModulus = ServerRSAPublicKey.substring(40, 194);
String ServerExponent = ServerRSAPublicKey.substring(214, 219);
BigInteger bigInteger = new BigInteger(ServerModulus, 10);
BigInteger bigInteger1 = new BigInteger(ServerExponent);

RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(bigInteger, bigInteger1);
KeyFactory keyFactory = null;
try {
    keyFactory = KeyFactory.getInstance("RSA");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
RSAPublicKey publicKeyOfServer = null;
try {
    assert keyFactory != null;
    publicKeyOfServer = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
} catch (InvalidKeySpecException e) {
    e.printStackTrace();
}

Cipher c = null;
try {
    c = Cipher.getInstance("RSA");
} catch (NoSuchPaddingException e) {
    e.printStackTrace();
}
try {
    assert c != null;
    c.init(Cipher.ENCRYPT_MODE, publicKeyOfServer);
} catch (InvalidKeyException e) {
    e.printStackTrace();
}

byte[] encryptedBytes = null;
try {
    encryptedBytes = c.doFinal(ClientSecretKey.getEncoded());
} catch (IllegalBlockSizeException | BadPaddingException e) {
    e.printStackTrace();
}
OutputStream outputStream;
outputStream = client.getOutputStream();
assert encryptedBytes != null;
outputStream.write(encryptedBytes, 0, encryptedBytes.length);

outputStream.flush();
outputStream.close();
client.close(); // closing the connection
