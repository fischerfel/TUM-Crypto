public class DataEncryptor 
{
    static char[] carr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static synchronized String encryptPassword(String input) 
    {

        MessageDigest digest = null;

        try 
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }

        if (digest == null)
            return input;

        try 
        {
            digest.update(input.getBytes("UTF-8"));
        }
        catch (java.io.UnsupportedEncodingException ex) 
        {
            ex.printStackTrace();
        }

        byte[] rawData = digest.digest();
        BASE64Encoder bencoder = new BASE64Encoder();
        return bencoder.encode(rawData);
    }
}
