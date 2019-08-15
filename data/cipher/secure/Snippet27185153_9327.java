public static void main(String[] args) throws Exception {  
  byte[] plaintext = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  byte[] ciphertext = encrypt(plaintext, "foo");

  byte[] goodDecryption = decrypt(ciphertext, "foo");
  byte[] badDecryption = decrypt(ciphertext, "bar");  

  System.out.println(DatatypeConverter.printHexBinary(goodDecryption));
  System.out.println(DatatypeConverter.printHexBinary(badDecryption));
}

public static SecretKey makeKey(String key) throws GeneralSecurityException {
  char[] password = key.toCharArray();
  byte[] salt = "12345678".getBytes();
  SecretKeyFactory factory =
      SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
  SecretKey tmp = factory.generateSecret(spec);
  return new SecretKeySpec(tmp.getEncoded(), "AES");
}

public static byte[] encrypt(byte[] plaintext, String key) throws Exception {
  SecretKey secret = makeKey(key);
  Cipher cipher = Cipher.getInstance("AES/OFB8/NoPadding");
  cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
  return cipher.doFinal(plaintext);
}

public static byte[] decrypt(byte[] ciphertext, String key) throws Exception {
  SecretKey secret = makeKey(key);
  Cipher cipher = Cipher.getInstance("AES/OFB8/NoPadding");
  cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
  return cipher.doFinal(ciphertext);
}
