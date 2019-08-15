String beforeEncrypt = "hitesh";
String afterEncrypt = "";
try
{
    //Create MessageDigest object for MD5
    MessageDigest digest = MessageDigest.getInstance("MD5");

    //Update input string in message digest
    digest.update(beforeEncrypt.getBytes(), 0, beforeEncrypt.length());

    afterEncrypt = new BigInteger(1, digest.digest()).toString();

    System.out.println(afterEncrypt);

} 
catch (NoSuchAlgorithmException e)
{
        e.printStackTrace();
}
