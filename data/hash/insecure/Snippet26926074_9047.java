public static byte[] convertImageToByteArray(Bitmap bitmap, boolean compress)
{
    if (compress)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    else
    {
        int bytes = ImageTools.getByteCount(bitmap);
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
}

public static String getPictureMd5Hash(Bitmap bitmap, boolean compress, BooleanHolder error)
{
    error.set(false);
    try
    {
        return getInternalPictureMd5Hash(bitmap, compress);
    }
    catch (OutOfMemoryError e)
    {
        error.set(true);
        return null;
    }
}

private static String getInternalPictureMd5Hash(Bitmap bitmap, boolean compress) throws OutOfMemoryError
{
    if (bitmap == null)
        return null;

    byte[] bitmapBytes = convertImageToByteArray(bitmap, compress);

    String s;
    try
    {
        s = new String(bitmapBytes, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
        L.e(Updater.class, e);
        return null;
    }

    MessageDigest m = null;

    try
    {
        m = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e)
    {
        L.e(Updater.class, e);
    }

    m.update(s.getBytes(), 0, s.length());
    return calcHash(m);
}

private static String calcHash(MessageDigest m)
{
    return new BigInteger(1, m.digest()).toString(16);
}
