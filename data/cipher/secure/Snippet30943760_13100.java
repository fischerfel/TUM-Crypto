public static byte[] encrypt(byte[] text, PublicKey key) throws Exception {
  final Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");

  // encrypt the plain text using the public key
  cipher.init(Cipher.ENCRYPT_MODE, key);
  return cipher.doFinal(text);
}
