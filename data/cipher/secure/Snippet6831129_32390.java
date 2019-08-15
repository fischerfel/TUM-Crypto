byte[] key = "MARTIN_123456789".getBytes("UTF-8");
byte[] iv = "1234567890123456".getBytes("UTF-8");
byte[] decryptedData = decrypt(key, iv, b);

private byte[] decrypt(byte[] raw, byte[] iv, byte[] encrypted) throws Exception {
  SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  IvParameterSpec ivspec = new IvParameterSpec(iv);         
  cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
  byte[] decrypted = cipher.doFinal(encrypted);

  return decrypted;
}
