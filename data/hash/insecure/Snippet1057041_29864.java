public static String getMD5Hash(String string)
{
    try 
    {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(string.getBytes());
        byte[] digest = md5.digest();

        string = byteArrToHexString(digest);
    } 
    catch (NoSuchAlgorithmException e1) 
    {
        e1.printStackTrace();
    }

    return string;
}

private static String byteArrToHexString(byte[] bArr)
{
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < bArr.length; i++) 
    {
        int unsigned = bArr[i] & 0xff;
        sb.append(Integer.toHexString((unsigned)));
    }

    return sb.toString();
}
