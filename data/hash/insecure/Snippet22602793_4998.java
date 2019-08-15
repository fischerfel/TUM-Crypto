public static String getMd5OfFile(String filePath)
{
    String returnVal = "";
    try 
    {
        InputStream   input   = new FileInputStream(filePath); 
        byte[]        buffer  = new byte[1024];
        MessageDigest md5Hash = MessageDigest.getInstance("MD5");
        int           numRead = 0;
        while (numRead != -1)
        {
            numRead = input.read(buffer);
            if (numRead > 0)
            {
                md5Hash.update(buffer, 0, numRead);
            }
        }
        input.close();

        byte [] md5Bytes = md5Hash.digest();
        for (int i=0; i < md5Bytes.length; i++)
        {
            returnVal += Integer.toString( ( md5Bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
    } 
    catch(Throwable t) {t.printStackTrace();}
    return returnVal.toUpperCase();
}
