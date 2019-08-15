def encryption(text) {
   String publicKeyModulus = "MODULUS_GOES_HERE";
   String exponent = "65537";
   RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(publicKeyModulus, 16), new BigInteger(exponent, 10));
   KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
   publicKey = publicKeyFactory.generatePublic(publicKeySpec);

   Cipher cipher = Cipher.getInstance("RSA");
   cipher.init(Cipher.ENCRYPT_MODE, publicKey);
   byte[] cipherData = cipher.doFinal(text.getBytes());

   return cipherData.encodeBase64().toString()
}
