public static String toSHA1Hash(byte[] stringAsBytearray)
{
    MessageDigest md = null;

    try
    {
        md = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException e)
    {
        Logger.WriteLog(e.toString());
    }

    if (md == null)
        return null;


    md.reset();
    md.update(stringAsBytearray);

    byte[] byteData = md.digest();
    StringBuilder sb = new StringBuilder();

    for (byte currByte : byteData)
        sb.append(Integer.toString((currByte & 0xff) + 0x100, 16).substring(1));

    return sb.toString();
}

public static String toSHA1Hash(String str)
{
    return toSHA1Hash(str.getBytes());
}

public static String toMySQLPasswordHash(String str)
{
    String hash1 = toSHA1Hash(str);
    byte[] unhexedHash1 = DatatypeConverter.parseHexBinary(hash1);
    String hash2 = toSHA1Hash(unhexedHash1);

    String passwordHash = "*" + hash2.toUpperCase();


    return passwordHash;
}
