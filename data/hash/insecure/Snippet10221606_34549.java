public static String getMD5(String input) 
{
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(input.getBytes());

      byte byteData[] = md.digest();

      //convert the byte to hex format method 1
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < byteData.length; i++)
      {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,
                                   16).substring(1));
      }
      return sb.toString();
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
}
