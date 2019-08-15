public static String getMD5Hash(String str) throws Exception
{
    MessageDigest md = MessageDigest.getInstance("MD5");

    md.update(str.getBytes());
    return new String(md.digest());
}
