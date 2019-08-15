public String hash(String pass) throws Exception
{
    encr = MessageDigest.getInstance("MD5");
    return new BigInteger(1, encr.digest(pass.getBytes())).toString(16);
}
