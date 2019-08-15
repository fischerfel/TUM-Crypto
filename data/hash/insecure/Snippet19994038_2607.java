public String getMd5(File file)
{
    DigestInputStream stream = null;
    try
    {
        stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
        byte[] buffer = new byte[65536];

        read = stream.read(buffer);
        while (read >= 1) {
        read = stream.read(buffer);
        }
    }
    catch (Exception ignored)
    {
        int read;
        return null;
    }
    return String.format("%1$032x", new Object[] { new BigInteger(1, stream.getMessageDigest().digest()) });
}
