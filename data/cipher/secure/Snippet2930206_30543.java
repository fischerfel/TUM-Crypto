keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(new FileInputStream("C:\\my.keystore"), "mypass".toCharArray());
Key key = keyStore.getKey("myalias", "mypass".toCharArray());
if (key instanceof PrivateKey) {
    Certificate cert = keyStore.getCertificate("myalias");
    PublicKey pubKey = cert.getPublicKey();
    privKey = (PrivateKey)key;
}
byte[] toDecodeBytes = new BigInteger(encodeMessageHex, 16).toByteArray();
Cipher decCipher = Cipher.getInstance("RSA");
decCipher.init(Cipher.DECRYPT_MODE, privKey);
byte[] decodeMessageBytes = decCipher.doFinal(toDecodeBytes);
String decodeMessageString = new String(decodeMessageBytes);
