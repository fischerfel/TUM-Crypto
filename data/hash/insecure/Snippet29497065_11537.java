    import java.security.DigestException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;


    public class DigestWrapper extends MessageDigest
    {
        private final MessageDigest md5;
        private final MessageDigest sha1;

        // some methods missing.
        // I just implemeted them throwing a RuntimeException.

        public DigestWrapper() throws NoSuchAlgorithmException
        {
            super(null);
            sha1 = MessageDigest.getInstance("sha-1");
            md5 = MessageDigest.getInstance("md5");
        }

        public byte[] getMD5Digest()
        {
            return md5.digest();
        }

        public byte[] getSHA1Digest()
        {
            return sha1.digest();
        }

        @Override
        public int digest(byte[] buf, int offset, int len) throws DigestException
        {
            md5.digest(buf, offset, len);
            sha1.digest(buf, offset, len);
            return 0;
        }

        @Override
        public byte[] digest(byte[] input)
        {
            md5.digest(input);
            sha1.digest(input);
            return input;
        }

        @Override
        public void reset()
        {
            md5.reset();
            sha1.reset();
        }

        @Override
        public void update(byte input)
        {
            md5.update(input);
            sha1.update(input);
        }

        @Override
        public void update(byte[] input, int offset, int len)
        {
            md5.update(input, offset, len);
            sha1.update(input, offset, len);
        }

        @Override
        public void update(byte[] input)
        {
            md5.update(input);
            sha1.update(input);
        }

    }
