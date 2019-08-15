public static String getMd5Hash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
{
  MessageDigest md = MessageDigest.getInstance("MD5");
  byte[] thedigest = md.digest(str.getBytes("UTF-8"));

  StringBuilder hexString = new StringBuilder();

  for (int i = 0; i < thedigest.length; i++)
  {
      String hex = Integer.toHexString(0xFF & thedigest[i]);
      if (hex.length() == 1)
          hexString.append('0');

      hexString.append(hex);
  }

  return hexString.toString().toUpperCase();
}
