public static final synchronized String encrypt(String plaintext, String algorithm, String encoding) throws Exception
{
  MessageDigest msgDigest = null;
  String hashValue = null;
  try
  {
    msgDigest = MessageDigest.getInstance(algorithm);
    msgDigest.update(plaintext.getBytes(encoding));
    byte rawByte[] = msgDigest.digest();
    hashValue = (new BASE64Encoder()).encode(rawByte);

  }
  catch (NoSuchAlgorithmException e)
  {
    System.out.println("No Such Algorithm Exists");
  }
  catch (UnsupportedEncodingException e)
  {
    System.out.println("The Encoding Is Not Supported");
  }
  return hashValue;
}
