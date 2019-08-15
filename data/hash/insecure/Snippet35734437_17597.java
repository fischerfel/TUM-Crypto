public static String getHash(String pass) throws Exception
{
    MessageDigest md=MessageDigest.getInstance("MD5");
    md.update(pass.getBytes(),0,pass.length());
    return new BigInteger(1,md.digest()).toString(16);
}
