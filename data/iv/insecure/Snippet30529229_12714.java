@Test
public void testAes() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
  byte[] key = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();
  byte[] data = "x".getBytes();
  byte[] iv = "1111111111111111".getBytes();
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
  IvParameterSpec ivspec = new IvParameterSpec(iv);
  cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivspec);
  byte[] result = cipher.doFinal(data);
  assertEquals("0eddfe1857248c7057904455d189cf31", DatatypeConverter.printHexBinary(result).toLowerCase());
}
