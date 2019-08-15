public static void SHA1(String x) throws NoSuchAlgorithmException
{

    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    SHA1 = sha1.digest((x).getBytes()); 

}
