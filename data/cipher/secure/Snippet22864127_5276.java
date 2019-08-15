public String getDecrypt(final byte[] encrypted) throws GeneralSecurityException, NullPointerException {
  if (key.isEmpty())
    throw new NullPointerException();

  final byte[] rawData = key.getBytes(Charset.forName("US-ASCII"));
  if (rawData.length != 16) {
    // If this is not 16 in length, there's a problem with the key size, nothing to do here
    throw new IllegalArgumentException("Invalid key size.");
  }

  final SecretKeySpec seckeySpec = new SecretKeySpec(rawData, "AES");

  final Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
  ciph.init(Cipher.DECRYPT_MODE, seckeySpec, new IvParameterSpec(new byte[16]));
  final byte[] decryptedmess = ciph.doFinal(encrypted);

  return new String(decryptedmess, Charset.forName("US-ASCII"));
}
