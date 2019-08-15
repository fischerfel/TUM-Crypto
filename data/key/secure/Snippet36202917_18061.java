 public static byte[] encrypt(byte[] plainText, byte[] key)  {
try {
  byte[] passwordKey128 = Arrays.copyOfRange(key, 0, 16);
  SecretKeySpec secretKey = new SecretKeySpec(passwordKey128, "AES");
  Cipher cipher = Cipher.getInstance("AES");
  cipher.init(Cipher.ENCRYPT_MODE, secretKey);
  byte[] cipherText = cipher.doFinal(plainText);
  // String encryptedString = Base64.getEncoder().encodeToString(cipherText);
  return cipherText;
