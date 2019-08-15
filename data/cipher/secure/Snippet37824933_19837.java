Socket client = new Socket("192.168.1.169", 4444);

int bytesRead;
int current = 0;
FileOutputStream fos;
byte[] EncryptedServerAES = new byte[100000];
InputStream is = client.getInputStream();
fos = new FileOutputStream("ServerAESKeyFile");
BufferedOutputStream bos = new BufferedOutputStream(fos);
bytesRead = is.read(EncryptedServerAES, 0, EncryptedServerAES.length);
current = bytesRead;
do {
    bytesRead =
            is.read(EncryptedServerAES, current, (EncryptedServerAES.length - current));
    if (bytesRead >= 0) current += bytesRead;
} while (bytesRead > -1);

bos.write(EncryptedServerAES, 0, current);
bos.flush();
fos.close();
bos.close();
client.close(); // closing the connection

// try to decrypt and get AES key back.
BigInteger Modulus;
BigInteger publicExponent;
BigInteger privateExponent;
BigInteger primeP;
BigInteger primeQ;
BigInteger primeExpP;
BigInteger primeExpQ;
BigInteger crtCoeff;
Modulus = new BigInteger(ClientPrivateKey.substring(32, 161));
publicExponent = new BigInteger("10001");
privateExponent = new BigInteger(ClientPrivateKey.substring(198, 327));
primeP = new BigInteger(ClientPrivateKey.substring(334, 399));
primeQ = new BigInteger(ClientPrivateKey.substring(406, 471));
primeExpP = new BigInteger(ClientPrivateKey.substring(486, 551));
primeExpQ = new BigInteger(ClientPrivateKey.substring(566, 631));
crtCoeff = new BigInteger(ClientPrivateKey.substring(646, 711));

Cipher cipher = Cipher.getInstance("RSA");
// realise client's private key string as private key.
RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(Modulus, publicExponent, privateExponent, primeP, primeQ, primeExpP, primeExpQ, crtCoeff);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
RSAPrivateKey ClientPrivateKEY = null;
try {
    ClientPrivateKEY = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivateCrtKeySpec);
} catch (InvalidKeySpecException e) {
    e.printStackTrace();
}
try {
    cipher.init(Cipher.DECRYPT_MODE, ClientPrivateKEY);
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
try {
    decryptedAESKeyOfServer = cipher.doFinal(EncryptedServerAES);
} catch (IllegalBlockSizeException | BadPaddingException e) {
    e.printStackTrace();
}
