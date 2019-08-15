public static void main(final String[] args) throws Exception
{
    final MessageDigest md5 = MessageDigest.getInstance("MD5");
    final byte[] digest = md5.digest("Grommit".getBytes("UTF-8"));
    long result = 0;
    for (int i = 0; i < 8; i++)
    {
        System.out.println(Long.toHexString(0xFFL & digest[i]));
        result |= (0xFFL & digest[i]) << (i * 8);
    }
    System.out.println(Long.toHexString(result));
}
