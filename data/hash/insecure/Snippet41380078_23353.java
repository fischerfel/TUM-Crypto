    private String getMD5(String filePath)
{
    String base64Digest = "";
    try
    {
        InputStream input   = new FileInputStream(filePath);
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
        base64Digest = Base64.encodeToString(md5Bytes, Base64.DEFAULT);

       /*for (byte md5Byte : md5Bytes) {
            returnVal += Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1);
        }*/
    }
    catch(Throwable t) {t.printStackTrace();}
    return base64Digest;
