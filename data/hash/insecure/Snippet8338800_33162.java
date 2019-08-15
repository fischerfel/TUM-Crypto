private static char[] hexDigits = "0123456789abcdef".toCharArray();

public String md5(InputStream is) throws IOException
{
    byte[] bytes = new byte[4096];
    int read = 0;
    MessageDigest digest = MessageDigest.getInstance("MD5");
    while ((read = is.read(bytes)) != -1)
    {
        digest.update(bytes, 0, read);
    }

    byte[] messageDigest = digest.digest();

    StringBuilder sb = new StringBuilder(32);

    // Oh yeah, this too.  Integer.toHexString doesn't zero-pad, so
    // (for example) 5 becomes "5" rather than "05".
    for (byte b : messageDigest)
    {
        sb.append(hexDigits[(b >> 4) & 0x0f]);
        sb.append(hexDigits[b & 0x0f]);
    }

    return sb.toString();
}
