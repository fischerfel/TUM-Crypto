public class MultipleDigestOutputStream extends FilterOutputStream
{

    public static final String[] DEFAULT_ALGORITHMS = { EncryptionConstants.ALGORITHM_MD5,
                                                        EncryptionConstants.ALGORITHM_SHA1 };

    private Map<String, MessageDigest> digests = new LinkedHashMap<>();

    private File file;


    public MultipleDigestOutputStream(File file, OutputStream os)
            throws NoSuchAlgorithmException, FileNotFoundException
    {
        this(file, os, DEFAULT_ALGORITHMS);
    }

    public MultipleDigestOutputStream(File file, OutputStream os, String[] algorithms)
            throws NoSuchAlgorithmException, FileNotFoundException
    {
        // super(file); // If extending FileOutputStream
        super(os);

        this.file = file;

        for (String algorithm : algorithms)
        {
            addAlgorithm(algorithm);
        }
    }

    public void addAlgorithm(String algorithm)
            throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        digests.put(algorithm, digest);
    }

    public MessageDigest getMessageDigest(String algorithm)
    {
        return digests.get(algorithm);
    }

    public Map<String, MessageDigest> getDigests()
    {
        return digests;
    }

    public String getMessageDigestAsHexadecimalString(String algorithm)
    {
        return MessageDigestUtils.convertToHexadecimalString(getMessageDigest(algorithm));
    }

    public void setDigests(Map<String, MessageDigest> digests)
    {
        this.digests = digests;
    }


    @Override
    public void write(int b)
            throws IOException
    {
        super.write(b);

        System.out.println("write(int b)");

        for (Map.Entry entry : digests.entrySet())
        {
            int p = b & 0xFF;
            byte b1 = (byte) p;

            MessageDigest digest = (MessageDigest) entry.getValue();
            digest.update(b1);
        }
    }

    @Override
    public void write(byte[] b)
            throws IOException
    {
        super.write(b);

        for (Map.Entry entry : digests.entrySet())
        {
            MessageDigest digest = (MessageDigest) entry.getValue();
            digest.update(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len)
            throws IOException
    {
        super.write(b, off, len);

        for (Map.Entry entry : digests.entrySet())
        {
            MessageDigest digest = (MessageDigest) entry.getValue();
            digest.update(b, off, len);
        }
    }

    @Override
    public void close()
            throws IOException
    {
        super.close();
    }

}
