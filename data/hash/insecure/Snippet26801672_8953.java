public static void main(String[] args)
{
    MessageDigest messageDigest = null;
    String ALGORITHM = "SHA";
    try
    {
        messageDigest = MessageDigest.getInstance(ALGORITHM);

        byte[] arr = "admin1!".getBytes();
        byte[] arr2 = messageDigest.digest(arr);

        System.out.println(arr2);
        String encoded = Base64.encodeBase64String(arr2);

        System.out.println(encoded);
        byte[] decoded = Base64.decodeBase64(encoded);

        System.out.println(decoded);
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
}
