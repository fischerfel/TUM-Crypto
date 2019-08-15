String MAC = hmacHelper.calculatePlainMAC("00000000", "HmacSHA256");

String bgSecretKey="1234567890ABCDEF1234567890ABCDEF";

public String calculatePlainMAC(String ascii, String algorithm)
{
  Mac mac = null;
  final Charset asciiCs = Charset.forName("US-ASCII");
  try
  {
    SecretKeySpec signingKey = new SecretKeySpec(bgcSecretKey.getBytes(), algorithm);
    mac = Mac.getInstance(algorithm);
    mac.init(signingKey);
    byte[] rawHmac = mac.doFinal(asciiCs.encode(ascii).array());

    String result = "";
    for (final byte element : rawHmac)
    {
      result += Integer.toString((element & 0xff) + 0x100, 16);//.substring(1);
    }
    log.debug("Result: " + result);
    return result;
  }
  catch (NoSuchAlgorithmException e)
  {
    e.printStackTrace();
    return null;
  }
  catch (InvalidKeyException e)
  {
    e.printStackTrace();
    return null;
  }
}
