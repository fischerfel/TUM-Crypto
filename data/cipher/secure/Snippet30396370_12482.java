public static byte[] RSAEncrypt(byte[] data) {
byte[] cipherData = null;
try {
PublicKey pubKey = readKeysFromFile();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
cipherData = cipher.doFinal(data);
}
catch (Exception e) {}

return cipherData;
}
