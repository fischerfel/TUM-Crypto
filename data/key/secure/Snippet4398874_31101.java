 public static String encrypt(byte[] key, String cleartext, boolean base64) throws Exception
 {
  byte[] rawKey   = key;
  byte[] result   = encrypt(rawKey, cleartext.getBytes());

  // Base 64
  if (base64)
   return toBase64(result);

  // Hex
  return toHex(result);
 }

 public static String decrypt(byte[] key, String encrypted)
   throws Exception
 {
  byte[] rawKey = key;
  byte[] enc    = toByte(encrypted);
  byte[] result = decrypt(rawKey, enc);

  return new String(result);
 }

 private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception
 {
  SecretKeySpec skeySpec          = new SecretKeySpec(raw, "AES");
  Cipher cipher                   = Cipher.getInstance("AES/ECB/NoPadding");

  cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

  byte[] encrypted = cipher.doFinal(clear);

  return encrypted;
 }
