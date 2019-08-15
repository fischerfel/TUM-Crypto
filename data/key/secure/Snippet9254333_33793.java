private static byte[] decryptPBKDF2WithBC(char[] password, byte[] data, byte[] salt, byte[] iv)
  throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
  InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

   PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
   generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt,CryptographicToolBox.CRYPTO_ITERATIONS);
   KeyParameter params = (KeyParameter)generator.generateDerivedParameters(CryptographicToolBox.KEY_SIZE);

   byte[] endcoded = params.getKey();
   SecretKey key = new SecretKeySpec(endcoded, "AES");

   Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");

   ciph.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv);
   return ciph.doFinal(data);
}
