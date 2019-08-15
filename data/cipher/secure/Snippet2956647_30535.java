public byte[] rsaEncrypt(byte[] data) {
  PublicKey pubKey = readKeyFromFile("/public.key");
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.ENCRYPT_MODE, pubKey);
  byte[] cipherData = cipher.doFinal(src);
  return cipherData;
}
