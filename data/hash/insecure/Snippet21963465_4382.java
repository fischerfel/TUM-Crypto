    String sha1 = "";
MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(bcopy);
        sha1 = byteToHex(crypt.digest());

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
