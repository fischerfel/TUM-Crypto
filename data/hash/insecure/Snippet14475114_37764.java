private static char[] hextable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

private static String md5(String s)
{
    MessageDigest digest;
    try
    {
        digest = MessageDigest.getInstance("MD5");
        digest.update(s.getBytes(), 0, s.length());
        byte[] bytes = digest.digest();

        String hash = "";
        for (int i = 0; i < bytes.length; ++i)
        {
            int di = (bytes[i] + 256) & 0xFF;
            hash = hash + hextable[(di >> 4) & 0xF] + hextable[di & 0xF];
        }

        return hash;
    }
    catch (NoSuchAlgorithmException e)
    {
    }

    return "";
}
