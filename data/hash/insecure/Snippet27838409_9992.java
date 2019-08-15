protected static String getMD5(String inputText)
{
    String md5 = "";
    try
    {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(inputText.getBytes());
        md5 = new BigInteger(1, digester.digest()).toString(16);
    }
    catch(Exception e)
    {
        SetLogInfo.writeLog("Exception: "+e);
    }
    return md5;
}
