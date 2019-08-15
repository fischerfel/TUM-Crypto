 public static byte[] encrypt(byte[] plainText, byte[] key) 
    {
  byte[] passwordKey128 = Arrays.copyOfRange(key, 0, 16);
  SecretKeySpec secretKey = new SecretKeySpec(passwordKey128, "AES");
  Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
  cipher.init(Cipher.ENCRYPT_MODE, secretKey);
  byte[] cipherText = cipher.doFinal(plainText);
  return cipherText;
 }
