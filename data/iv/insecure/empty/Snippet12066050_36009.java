public byte[] encryptData(byte[] key, byte[] data) {
  Cipher c = Cipher.getInstance("AES/CTR/PKCS5PADDING");
  byte[] initialCounter = new byte[16];
  c.init(Cipher.ENCRYPT_MODE,
    new SecretKeySpec(key, "AES"),
    new IvParameterSpec(initialCounter));
  byte[] encryptedData = c.doFinal(plaintextData);
  return encryptedData;
}
