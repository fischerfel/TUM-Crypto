public static PublicKey getPublicKey(final BigInteger modulus, final BigInteger exponent) {
    try {
      final KeyFactory factory = KeyFactory.getInstance("RSA");
      final PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
      return publicKey;
    } catch (GeneralSecurityException e) {
      throw new BaseException(e);
    }
}
public static String encryptPAN(final String prefix, final String pan, PublicKey publicKey) {
   byte[] input = String.format("%s%s", prefix, pan).getBytes();
   try {
     Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
     cipher.init(Cipher.ENCRYPT_MODE, publicKey, RANDOM);
     byte[] cipherText = cipher.doFinal(input);
     return DatatypeConverter.printBase64Binary(cipherText);
   } catch (GeneralSecurityException ignore) {
     return null;
   }
 }
