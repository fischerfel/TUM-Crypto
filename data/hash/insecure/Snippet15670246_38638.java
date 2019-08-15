public abstract class Sha1Util
{
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static MessageDigest newSha1Digest()
    {
        try
        {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e)
        {
            throw new Error(e);
        }
    }

    public static void update(final MessageDigest digest, final String s)
    {
        digest.update(s.getBytes(UTF8));
    }

    public static String sha1sum(final MessageDigest digest)
    {
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}
