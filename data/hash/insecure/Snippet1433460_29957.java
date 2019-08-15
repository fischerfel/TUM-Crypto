public static String signature(Object o)
{
    StringBuffer sb = new StringBuffer();
    try
    {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        String st = "SECRET!!1" + o.hashCode();
        md5.update(st.getBytes());
        sb.append(getHexString(md5.digest()));
    }
    catch (NoSuchAlgorithmException e)
    {
        throw new RuntimeException("bah");
    }
    catch (UnsupportedEncodingException e)
    {
        throw new RuntimeException("bah2");
    }
    return sb.toString();
}

static final byte[] HEX_CHAR_TABLE =
{
        (byte) '0', (byte) '1', (byte) '2', (byte) '3', 
        (byte) '4', (byte) '5', (byte) '6', (byte) '7', 
        (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
};

public static String getHexString(byte[] raw) throws UnsupportedEncodingException
{
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw)
    {
        int v = b & 0xFF;
        hex[index++] = HEX_CHAR_TABLE[v >>> 4];
        hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }
    return new String(hex, "ASCII");
}
