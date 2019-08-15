public String md5ForBitmap(Bitmap bitmap)
{
    String hash = "";
    try
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bitmapBytes);
        byte[] digestedBytes = messageDigest.digest();

        BigInteger intRep = new BigInteger(1, digestedBytes);
        hash = intRep.toString(16);
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    return hash;
}
