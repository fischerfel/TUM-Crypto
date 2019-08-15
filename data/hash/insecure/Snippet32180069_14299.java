public static String toSHA1Hash(String str)
{
    MessageDigest md = null;

    try
    {
        md = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException e)
    {
        Logger.WriteLog(e.toString());
    }

    if (md == null)
        return null;


    md.reset();
    md.update(str.getBytes());

    byte[] byteData = md.digest();
    StringBuilder sb = new StringBuilder();

    for (byte currByte : byteData)
        sb.append(Integer.toString((currByte & 0xff) + 0x100, 16).substring(1));

    return sb.toString();
}
