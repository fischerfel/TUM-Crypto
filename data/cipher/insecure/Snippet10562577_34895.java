  public static byte[] decrypted_Data(byte[] crypt) throws Exception {

  String seed = "SuperSecretPassword";

  KeyGenerator keygen = KeyGenerator.getInstance("AES");

  SecureRandom secrand = SecureRandom.getInstance("SHA1PRNG");

  secrand.setSeed(seed.getBytes());

  keygen.init(128, secrand);

  SecretKey seckey = keygen.generateKey();

  byte[] rawKey = seckey.getEncoded();

 SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");

 Cipher cipher = Cipher.getInstance("AES");

 cipher.init(Cipher.DECRYPT_MODE, skeySpec);

 byte[] decrypted = cipher.doFinal(crypt);

  return decrypted;
}
