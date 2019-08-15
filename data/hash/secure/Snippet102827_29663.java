/**
     * Returns the hash value of the given chars
     * 
     * Uses the default hash algorithm described above
     * 
     * @param in
     *            the byte[] to hash
     * @return a byte[] of hashed values
     */
    public static byte[] getHashedBytes(byte[] in)
    {
        MessageDigest msg;
        try
        {
            msg = MessageDigest.getInstance(hashingAlgorithmUsed);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new AssertionError("Someone chose to use a hashing algorithm that doesn't exist.  Epic fail, go change it in the Util file.  SHA(1) or MD5");
        }
        msg.update(in);
        return msg.digest();
    }
