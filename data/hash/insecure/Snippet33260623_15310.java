public static String getSha1Hex(String clearString)
{
    try
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(clearString.getBytes("UTF-8"));
        byte[] bytes = messageDigest.digest();
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes)
        {
            buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return buffer.toString();
    }
    catch (Exception ignored)
    {
        ignored.printStackTrace();
        return null;
    }
}
