try
{
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    //byte[] buffer = new byte[65536]; //created at start.
    final FileInputStream fis = new FileInputStream(filename);
    int n = 0;
    byte[] buffer = null;
    while (n != -1)
    {
        n = fis.read(buffer);
        if (n > 0)
        {
            digest.update(buffer, 0, n);
        }
    }
    byte[] digestResult = digest.digest();
    return digestResult;
}
catch (Exception e)
{
    return null;
}
