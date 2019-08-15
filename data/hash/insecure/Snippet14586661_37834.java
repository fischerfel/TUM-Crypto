public class SecureDigester
{
    private static final char digits[] =
           { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                 'F' };

           private static String byteArrayToHexString(byte[] b)
           {
              StringBuffer hexString = new StringBuffer(b.length);
              for (int i = 0; i < b.length; i++)
              {
                 hexString.append(digits[(b[i] & 0xF0) >> 4]);
                 hexString.append(digits[b[i] & 0x0F]);
              }
              return hexString.toString();
           }

           public static String digest(String plaintext)
           {
              try
              {
                 MessageDigest md = MessageDigest.getInstance("SHA");
                 md.update(plaintext.getBytes("UTF-8"));
                 byte[] mdBytes = md.digest();
                 String hashString = byteArrayToHexString(mdBytes);
                 return hashString;
              } catch (Exception e)
              {
                 throw new RuntimeException(e);
              }
           }

}
