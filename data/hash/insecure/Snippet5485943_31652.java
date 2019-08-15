public static String hashDataAsString(String dataToHash)
{
    MessageDigest messageDigest;
    try
    {
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        byte[] data = hexStringToByteArray(dataToHash);
        messageDigest.update(data);
        final byte[] resultByte = messageDigest.digest();
        return new String(Hex.encodeHex(resultByte));
    }
    catch(NoSuchAlgorithmException e)
    {
        throw new RuntimeException("Failed to hash data values", e);
    }
}
