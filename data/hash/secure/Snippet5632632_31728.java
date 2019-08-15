private static String DIGEST_ALGORITHM = "SHA-512";

    public static byte[] getHash(final byte[] data, final byte[] salt) throws NoSuchAlgorithmException
{
    final MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
    md.reset();
    if (salt != null)
    {
        md.update(salt);
    }
    return md.digest(data);
