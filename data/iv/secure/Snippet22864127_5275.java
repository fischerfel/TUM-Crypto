public String getEncrypt(final byte[] iv, final String message) throws GeneralSecurityException, NullPointerException {
  if (key.isEmpty())
    throw new NullPointerException();

  final byte[] rawData = key.getBytes(Charset.forName("US-ASCII"));
  if (rawData.length != 16) {
    // If this is not 16 in length, there's a problem with the key size, nothing to do here
    throw new IllegalArgumentException("You've provided an invalid key size");
  }

  final SecretKeySpec seckeySpec = new SecretKeySpec(rawData, "AES");
  final Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");

  ciph.init(Cipher.ENCRYPT_MODE, seckeySpec, new IvParameterSpec(iv));

  byte[] encryptedBA = ciph.doFinal(message.getBytes(Charset.forName("US-ASCII")));
  try {
    final String encryptedText = new String(Base64.encode(encryptedBA, Base64.DEFAULT), "UTF-8");
    return encryptedText.toString();
  } 
  catch (final UnsupportedEncodingException e1) { }
  return "";
}
