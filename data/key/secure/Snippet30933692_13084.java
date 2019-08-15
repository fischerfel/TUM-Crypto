public byte[] encrypt(byte[] data, byte[] key) {
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
  return cipher.doFinal(data);
}
