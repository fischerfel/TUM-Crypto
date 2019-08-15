private static String encryptPassword(String password)
{
    String sha1 = "";
    try
    {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));
        sha1 = byteToHex(crypt.digest());
    }
    catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch(UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }
    return sha1;
}

private static String byteToHex(final byte[] hash)
{
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
        formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
}
