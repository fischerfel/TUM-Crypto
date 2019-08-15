public static String makeCheck(String url)
{
    URL u=new URL(url);
    MessageDigest md = MessageDigest.getInstance("MD5");
    u.getQuery();
    md.update(u.getQuery().getBytes());
    BigInteger bn = new BigInteger(1,md.digest("A_SECRET_WORD".getBytes()));
    return bn.toString(16);
}
