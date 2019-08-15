   public static SecretKey getAesKey(char[] password, int keyLength)
   throws GeneralSecurityException {

  int count = 128; // Iteration count
  byte[] salt;
  try {
   salt = "This is a fixed salt string".getBytes("UTF-8");
  } catch (UnsupportedEncodingException e) {
   throw new IllegalStateException("No UTF-8");
  }
  PBEKeySpec keySpec = new PBEKeySpec(password, salt, count, keyLength);
  SecretKeyFactory skf = SecretKeyFactory
    .getInstance("PBKDF2WithHmacSHA1");
  SecretKey pbeKey = skf.generateSecret(keySpec);
  byte[] raw = pbeKey.getEncoded();
  return new SecretKeySpec(raw, "AES");
 }
