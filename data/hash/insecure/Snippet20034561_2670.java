public static String sha1(String toHash)
{
    String hash = null;
    try
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = toHash.getBytes("ASCII"); //I tried UTF-8, ISO-8859-1...
        digest.update(bytes, 0, bytes.length);
        bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes)
        {
            sb.append(String.format("%02X", b));
        }
        hash = sb.toString();
    }
    catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch(UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }
    return hash.toLowerCase(Locale.ENGLISH);
}
